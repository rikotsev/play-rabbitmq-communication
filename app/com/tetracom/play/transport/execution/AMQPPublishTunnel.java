package com.tetracom.play.transport.execution;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.assistedinject.Assisted;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPInitializer;

import akka.japi.pf.FI.UnitApply;
import play.Logger;
import play.inject.ApplicationLifecycle;

class AMQPPublishTunnel extends AAMQPChannelHandler<IAMQPPublishTunnelConfig, IAMQPPublishAction> {

	private IAMQPPublishTunnelConfig config;

	@Inject
	public AMQPPublishTunnel(ApplicationLifecycle lifecycle, IAMQPInitializer initializer,
			IAMQPComponentsProvider componentsProvider, @Assisted Connection connection,
			@Assisted Class<IAMQPPublishTunnelConfig> configType, @Assisted Class<IAMQPPublishAction> actionType) {
		super(lifecycle, initializer, componentsProvider, connection, configType, actionType);
	}

	@Override
	protected UnitApply<IAMQPPublishTunnelConfig> handlerForConfig() {
		return new UnitApply<IAMQPPublishTunnelConfig>() {

			@Override
			public void apply(IAMQPPublishTunnelConfig configMsg) throws Exception {
				config = configMsg;
				doWork(config);
			}};
	}

	@Override
	protected UnitApply<IAMQPPublishAction> handlerForAction() {
		return new UnitApply<IAMQPPublishAction>() {

			@Override
			public void apply(IAMQPPublishAction actionMsg) throws Exception {
				doWork(actionMsg);
			}
			
		};
	}

	@Override
	protected void renewBindings() {
		if(config != null) {
			doWork(config);
		}
	}

	private void doWork(final IAMQPPublishTunnelConfig config) {
		final Optional<Channel> channel = getChannel();

		if (!channel.isPresent()) {
			Logger.error(
					"AMQP >> There was no channel to initialize the config on! If the channel closed silently KeepAlive should revive it and try to initialize the config again!");
			return;
		}

		try {

			if (config.getConfirmListener().isPresent()) {
				channel.get().addConfirmListener(config.getConfirmListener().get());
			}

			if (config.getReturnListener().isPresent()) {
				channel.get().addReturnListener(config.getReturnListener().get());
			}

		} catch (final Exception e) {
			Logger.error(
					"AMQP >> There was an issue binding listeners to the channel! If the channel closed silently KeepAlive should revive it and try to bind them again!");
		}
	}

	private void doWork(final IAMQPPublishAction action) {

		final Optional<Channel> channel = getChannel();

		if (!channel.isPresent()) {
			Logger.error(
					"AMQP >> There was no channel to publish to! If the channel close silently KeepAlive should revive it and you can try again!");
			
			if(action.noChannelHandler().isPresent()) {
				action.noChannelHandler().get().handle(action);
			}
			
			return;
		}

		final BasicProperties props = getBasicProperties(action);
		final boolean isMandatory = config.getReturnListener().isPresent();
		
		// Publishing to an exchange with a routing key
		if (action.queue().isPresent()) {
			action.messages().stream().forEach(msg -> {
				
				final String readableMessage = new String(msg, StandardCharsets.UTF_8);
				
				
				try {
					Logger.info("AMQP >> Attempting to publish to exchange {} with routing key {}",
							action.exchange().name(), action.queue().get().name());

					channel.get().basicPublish(action.exchange().name(), action.queue().get().routingKey(), isMandatory, props, msg);
					
					
				} catch (final Exception e) {

					Logger.error("AMQP >> Message sending failed for message {} with ", readableMessage, e);
				}
			});
		}
		// Publishing to an exchange without routing key - for a fanout exchange for
		// example
		else {
			action.messages().stream().forEach(msg -> {
				final String readableMessage = new String(msg, StandardCharsets.UTF_8);
				
				try {
					Logger.info("AMQP >> Atempting to publish to exchange {} without routing key",
							action.exchange().name());

					channel.get().basicPublish(action.exchange().name(), StringUtils.EMPTY, isMandatory, props, msg);
					
					
				} catch (final Exception e) {
					

					Logger.error("AMQP >> Message sending failed for message {} with ", readableMessage, e);
				}
			});
		}

	}

	private BasicProperties getBasicProperties(final IAMQPPublishAction action) {
		final BasicProperties.Builder builder = new BasicProperties.Builder();

		// This is not needed when we are not using a durable queue, however, if we
		// start using one it is important to be set
		builder.contentEncoding("application/json");

		if (action.replyTo().isPresent()) {
			builder.replyTo(action.replyTo().get().name());
		}

		return builder.build();

	}

}
