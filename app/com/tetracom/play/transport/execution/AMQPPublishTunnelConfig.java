package com.tetracom.play.transport.execution;

import java.util.Optional;

import org.springframework.util.Assert;

import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.ReturnListener;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPPublishTunnelConfig implements IAMQPPublishTunnelConfig {
	
	private IAMQPQueue queue;
	
	private IAMQPExchange exchange;
	
	private ConfirmListener confirmListener;
	
	private ReturnListener returnListener;
	
	public AMQPPublishTunnelConfig(final IAMQPExchange exchange) {
		//Both needed to pass through the mapping for the Publish Supervisor and be redirected to the correct tunnel
		Assert.notNull(exchange, "You cannot initialize a publish tunnel config without an exchange!");
		Assert.notNull(exchange.name(), "You cannot initialize a publish tunnel config with an exchange without a name!");
		
		this.exchange = exchange;
	}
	
	public AMQPPublishTunnelConfig(final IAMQPExchange exchange, final IAMQPQueue queue) {
		this(exchange);
		
		if(queue != null) {
			//Needed to pass through the mapping for the Subscribe Supervisor and be redirected to the correct tunnel
			Assert.notNull(queue.name(), "You cannot initialize a publish tunnel config with a queue without the name of the queue!");
		}
		
		this.queue = queue;
	}
	
	@Override
	public void setConfirmListener(ConfirmListener confirmListener) {
		this.confirmListener = confirmListener;
	}

	@Override
	public void setReturnListener(ReturnListener returnListener) {
		this.returnListener = returnListener;
	}
	
	public Optional<ConfirmListener> getConfirmListener() {
		return Optional.ofNullable(confirmListener);
	}
	
	public Optional<ReturnListener> getReturnListener() {
		return Optional.ofNullable(returnListener);
	}

	@Override
	public Optional<IAMQPQueue> queue() {
		return Optional.ofNullable(queue);
	}

	@Override
	public IAMQPExchange exchange() {
		return exchange;
	}
}
