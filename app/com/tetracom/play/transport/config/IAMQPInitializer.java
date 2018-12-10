package com.tetracom.play.transport.config;

import com.rabbitmq.client.Channel;

import play.Logger;

/**
 * Binds all the provided components so that if the channel uses any of them
 * they will exist
 * 
 * @author radoslav
 *
 */
public interface IAMQPInitializer {
	
	/**
	 * Initializes the communication environment
	 * @param channel - the channel currently being used
	 * @param componentsProvider - provides the component in the current communication scheme
	 */
	default void initialize(final Channel channel, final IAMQPComponentsProvider componentsProvider) {

		Logger.info("AMQP >> Initializing components of the communication");

		componentsProvider.provideExchanges().stream().forEach(exchange -> {

			try {
				Logger.info("AMQP >> Declaring exchange {}", exchange.name());
				channel.exchangeDeclare(exchange.name(), exchange.type());
			} catch (final Exception e) {
				Logger.error("AMQP >> Failed to intialize exchange {}", exchange.name());
			}

		});

		componentsProvider.provideQueues().stream().forEach(queue -> {
			try {
				Logger.info("AMQP >> Declaring queue {}", queue.name());
				channel.queueDeclare(queue.name(), queue.durable(), queue.exclusive(), queue.autoDelete(), null);

				Logger.info("AMQP >> Binding queue {} to exchange {}", queue.name(), queue.exchangeName());
				channel.queueBind(queue.name(), queue.exchangeName(), queue.routingKey());
			} catch (final Exception e) {
				Logger.error("AMQP >> Failed to initiliaze/bind queue {}", queue.name());
			}
		});

	};

}
