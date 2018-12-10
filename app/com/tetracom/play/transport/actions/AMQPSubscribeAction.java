package com.tetracom.play.transport.actions;

import java.util.Optional;

import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPSubscribeAction implements IAMQPSubscribeAction {
	
	private IAMQPQueue queue;
	
	private IDeliveryHandler deliveryHandler;
	
	private boolean autoAck;
	
	public AMQPSubscribeAction(final IAMQPQueue queue, final IDeliveryHandler handler) {
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
