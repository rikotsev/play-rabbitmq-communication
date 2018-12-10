package com.tetracom.play.transport.execution;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.inject.assistedinject.Assisted;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPInitializer;
import com.tetracom.play.transport.execution.SupervisorTunnelProtocol.KeepAlive;

import akka.actor.AbstractActor;
import akka.japi.pf.FI.UnitApply;
import play.Logger;
import play.inject.ApplicationLifecycle;

abstract class AAMQPChannelHandler<E, T> extends AbstractActor {

	private Connection connection;
	
	private IAMQPInitializer initializer;
	
	private IAMQPComponentsProvider componentsProvider;

	private Channel channel;

	private Class<E> configType;

	private Class<T> actionType;

	public AAMQPChannelHandler(final ApplicationLifecycle lifecycle, final IAMQPInitializer initializer, final IAMQPComponentsProvider componentsProvider, @Assisted Connection connection,
			@Assisted Class<E> configType, @Assisted Class<T> actionType) {

		this.connection = connection;
		this.initializer = initializer;
		this.componentsProvider = componentsProvider;

		this.configType = configType;
		this.actionType = actionType;

		lifecycle.addStopHook(() -> {

			terminate();

			return CompletableFuture.completedFuture(null);
		});
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SupervisorTunnelProtocol.KeepAlive.class, handlerForKeepAlive())
				.match(configType, handlerForConfig()).match(actionType, handlerForAction()).build();
	}

	abstract protected UnitApply<E> handlerForConfig();

	abstract protected UnitApply<T> handlerForAction();

	abstract protected void renewBindings();

	final protected Optional<Channel> getChannel() {
		try {
			if (channel == null) {
	
				return initChannel();
	
			} else if (!channel.isOpen()) {
				terminate();
				return initChannel();
			} else {
				return Optional.ofNullable(channel);
			}
		}
		catch(final Exception e) {
			Logger.error("AMQP >> Was not able to get an alive channel with", e);
			return Optional.empty();
		}
	}

	final private Optional<Channel> initChannel() throws IOException {
		if (connection == null || !connection.isOpen()) {
			return Optional.empty();
		}

		channel = connection.createChannel();		
		initializer.initialize(channel, componentsProvider);
		
		return Optional.ofNullable(channel);
	}

	final private void terminate() throws IOException {
		try {
			channel.close();
		} catch (final Exception e) {
			Logger.error("AMQP >> There was an issue with the channel", e);
		} finally {
			channel.abort();
		}
	}

	final private UnitApply<SupervisorTunnelProtocol.KeepAlive> handlerForKeepAlive() {
		return new UnitApply<SupervisorTunnelProtocol.KeepAlive>() {

			@Override
			public void apply(KeepAlive keepAlive) throws Exception {

				if (channel != null && channel.isOpen() && connection != null && connection.isOpen()) {
					// do nothing, we are alive and well
				} else {
					connection = keepAlive.getConnection();
					initChannel();
					renewBindings();
				}

			}

		};
	}

}
