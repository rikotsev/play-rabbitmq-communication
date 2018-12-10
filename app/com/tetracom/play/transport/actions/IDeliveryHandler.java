package com.tetracom.play.transport.actions;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

public interface IDeliveryHandler {
	boolean handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws Exception;
}
