package com.tetracom.play.transport.config;

import java.util.Map;

/**
 * Wrap around a RabbitMQ queue configuration
 * @author radoslav
 *
 */
public interface IAMQPQueue {
	/**
	 * The name of the queue, can be used when we need to listen for messages in a certain queue
	 * @return
	 */
	String name();
	
	/**
	 * Whether or not we want persistent queue, that saves the messages if something happens
	 * @return
	 */
	boolean durable();

	/**
	 * Queue is used by only 1 connection and it will be deleted after that connection finishes using it
	 * @return
	 */
	boolean exclusive();
	
	/**
	 * Queue that has had at least one consumer is deleted when last consumer unsubscribes
	 * @return
	 */
	boolean autoDelete();
	
	/**
	 * Things such as message TTL, queue length limit, etc
	 * @return
	 */
	Map<String, Object> arguments();
	
	/**
	 * The key that would indicate in the exchange to redirect to this queue
	 * @return
	 */
	String routingKey();
	
	/**
	 * The exchange on which we want to bind the queue
	 * @return
	 */
	String exchangeName();
}
