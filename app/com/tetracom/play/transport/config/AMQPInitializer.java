package com.tetracom.play.transport.config;

import com.rabbitmq.client.Channel;

import io.jsonwebtoken.lang.Assert;
import play.Logger;

/**
 * 
 * @author radoslav
 *
 */
class AMQPInitializer implements IAMQPInitializer {
	
	/**
	 * Initializes the communication environment
	 * @param channel - the channel currently being used
	 * @param componentsProvider - provides the component in the current communication scheme
	 */
	public void initialize(final Channel channel, final IAMQPComponentsProvider componentsProvider) {

		Logger.info("AMQP >> Initializing components of the communication");
		
		checkValidity(componentsProvider);
		
		if(componentsProvider.provideExchanges() != null) {
			componentsProvider.provideExchanges().stream().forEach(exchange -> {
	
				try {
					Logger.info("AMQP >> Declaring exchange {}", exchange.name());
					channel.exchangeDeclare(exchange.name(), exchange.type());
				} catch (final Exception e) {
					Logger.error("AMQP >> Failed to intialize exchange {}", exchange.name());
				}
	
			});
		}
		else {
			Logger.warn("AMQP >> There are no exchanges to be initialized!");
		}
		
		if(componentsProvider.provideQueues() != null) {
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
		}
		else {
			Logger.warn("AMQP >> There are no queues to be initialized!");
		}

	};
	
	/**
	 * Here the checks ensure there is a meaningful configuration in the application. 
	 * @param componentsProvider
	 */
	private void checkValidity(final IAMQPComponentsProvider componentsProvider) {
		componentsProvider.provideExchanges().stream().forEach(ex -> {
			Assert.notNull(ex, "It is not possible to initialize a null exchange!");
			Assert.notNull(ex.name(), "It is not possible to initialize an exchange without a name!");
			Assert.notNull(ex.type(), "It is not possible to initialize an exchange without a type!");
		});
		
		componentsProvider.provideQueues().stream().forEach(queue -> {
			Assert.notNull(queue, "It is not possible to declare a null queue");
			Assert.notNull(queue.name(), "It is not possible to declare a queue without a name!");
			Assert.notNull(queue.exchangeName(), "It is not possible to bind the queue without an exchange name to bind to!");
			Assert.notNull(queue.routingKey(), "It is not possible to bind the queue without a routing key!");
		});
	}
	
}
