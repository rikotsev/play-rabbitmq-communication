package com.tetracom.play.transport.execution;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPInitializer;

import akka.japi.pf.FI.UnitApply;
import play.Logger;
import play.inject.ApplicationLifecycle;

class AMQPSubscribeTunnel extends AAMQPChannelHandler<IAMQPSubscribeTunnelConfig, IAMQPSubscribeAction> {

	private IAMQPSubscribeTunnelConfig config;

	private IAMQPSubscribeAction action;

	@Inject
	public AMQPSubscribeTunnel(ApplicationLifecycle lifecycle, final IAMQPInitializer initializer,
			final IAMQPComponentsProvider componentsProvider, @Assisted @Nullable Connection connection,
			@Assisted Class<IAMQPSubscribeTunnelConfig> configType, @Assisted Class<IAMQPSubscribeAction> actionType) {
		super(lifecycle, initializer, componentsProvider, connection, configType, actionType);
	}

	@Override
	protected UnitApply<IAMQPSubscribeTunnelConfig> handlerForConfig() {
		return new UnitApply<IAMQPSubscribeTunnelConfig>() {

			@Override
			public void apply(IAMQPSubscribeTunnelConfig configMsg) throws Exception {
				config = configMsg;
				doWork(config);

			}

		};
	}

	@Override
	protected UnitApply<IAMQPSubscribeAction> handlerForAction() {
		return new UnitApply<IAMQPSubscribeAction>() {

			@Override
			public void apply(IAMQPSubscribeAction actionMsg) throws Exception {
				action = actionMsg;
				doWork(action);

			}

		};
	}

	@Override
	protected void renewBindings() {
		if(config != null) {
			doWork(config);
		}
		if(action != null) {
			doWork(action);
		}
		
	}

	private void doWork(final IAMQPSubscribeTunnelConfig config) {

		final Optional<Channel> channel = getChannel();

		if (!channel.isPresent()) {
			Logger.error(
					"AMQP >> There was no channel to initialize the config. When KeepAlive revives the channel, it will be initialized!");
			return;
		}

		try {
			if (config.getPrefetchCount().isPresent()) {
				channel.get().basicQos(config.getPrefetchCount().get());
			}
		} catch (final Exception e) {
			Logger.error(
					"AMQP >> There was a failure to initialize the config. If the channel closed silently KeepAlive will revive it and try to initiliaze it! Check if AMQP Broker is still running or if the config option is correct.");
		}
	}

	private void doWork(final IAMQPSubscribeAction action) {

		final Optional<Channel> channel = getChannel();

		if (!channel.isPresent()) {
			Logger.error(
					"AMQP >> There was no channel to execute the action on. When KeepAlive revives the channel the subscribe action will bind again!");
		}

		try {

			final boolean autoAck = action.getAutoAck().orElse(false);
			
			Logger.info("AMQP >> Attempting to subscribe to queue {}", action.queue().name());
			
			channel.get().basicConsume(action.queue().name(), autoAck, new DefaultConsumer(channel.get()) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					
					try {
						final boolean result = action.getDeliveryHandler().handleDelivery(consumerTag, envelope,
								properties, body);

						if (!autoAck && result) {
							getChannel().basicAck(envelope.getDeliveryTag(), false);
						} else {
							Logger.warn("AMQP >> Handling for message was no successful and autoAck is {}!", autoAck);
						}
						
					} catch (final Exception e) {
						Logger.error("AMQP >> Handling for message failed with exception and autoAck is {} ", autoAck,
								e);
					}

				}

			});

		} catch (final Exception e) {
			Logger.error(
					"AMQP >> There was a failure while binding the subscribe action. If the channel closed silently KeepAlive will revive it and try to bind it again! Check if the binding is correct.");
		}

	}
}
