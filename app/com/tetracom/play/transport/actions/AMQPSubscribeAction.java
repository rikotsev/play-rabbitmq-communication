package com.tetracom.play.transport.actions;

import java.util.Optional;

import com.tetracom.play.transport.config.IAMQPQueue;

import io.jsonwebtoken.lang.Assert;

class AMQPSubscribeAction implements IAMQPSubscribeAction {
	
	private IAMQPQueue queue;
	
	private IDeliveryHandler deliveryHandler;
	
	private boolean autoAck;
	
	public AMQPSubscribeAction(final IAMQPQueue queue, final IDeliveryHandler handler) {
		
		Assert.notNull(queue, "A subscribe action without a queue to subscribe to cannot be initialized!");
		Assert.notNull(queue.name(), "A subscribe action with a queue without a name cannot be initialized!");
		Assert.notNull(handler, "A subscribe action without a handler cannot be initialized!");
		
		this.queue = queue;
		this.deliveryHandler = handler;
	}
	
	@Override
	public IAMQPQueue queue() {
		return queue;
	}

	@Override
	public IAMQPSubscribeAction setAutoAck(boolean autoAck) {
		this.autoAck = autoAck;
		return this;
	}

	@Override
	public IDeliveryHandler getDeliveryHandler() {
		return deliveryHandler;
	}

	@Override
	public Optional<Boolean> getAutoAck() {
		return Optional.ofNullable(autoAck);
	}

}
