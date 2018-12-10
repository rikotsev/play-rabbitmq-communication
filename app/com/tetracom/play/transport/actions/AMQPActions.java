package com.tetracom.play.transport.actions;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

@Singleton
class AMQPActions implements IAMQPActions {
	
	@Inject
	public AMQPActions() {}

	@Override
	public IAMQPPublishAction publish(IAMQPExchange exchange) {
		return new AMQPPublishAction(exchange);
	}

	@Override
	public IAMQPPublishAction publish(IAMQPExchange exchange, IAMQPQueue queue) {
		return new AMQPPublishAction(exchange, queue);
	}

	@Override
	public IAMQPSubscribeAction subscribe(IAMQPQueue queue, IDeliveryHandler handler) {
		return new AMQPSubscribeAction(queue, handler);
	}
	
}
