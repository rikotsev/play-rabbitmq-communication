package com.tetracom.play.transport.config;

import com.rabbitmq.client.BuiltinExchangeType;

/**
 * Wraps around a basic RabbitMQ exchange
 * @author radoslav
 *
 */
public interface IAMQPExchange {
	/**
	 * The name of the exchange, used when we want to send messages
	 * @return
	 */
	String name();
	/**
	 * The type of the exchange
	 * @return
	 */
	BuiltinExchangeType type();
}
