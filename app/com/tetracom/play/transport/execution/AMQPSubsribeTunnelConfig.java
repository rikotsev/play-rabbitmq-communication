package com.tetracom.play.transport.execution;

import java.util.Optional;

import org.springframework.util.Assert;

import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPSubsribeTunnelConfig implements IAMQPSubscribeTunnelConfig {
	
	private IAMQPQueue queue;
	
	public AMQPSubsribeTunnelConfig(final IAMQPQueue subscribedQueue) {
		
		//Both needed for the Subscribe Supervisor in order to redirect to the right Subscribe Tunnel
		Assert.notNull(subscribedQueue, "Cannot initialize a subscribe tunnel config without a queue!");
		Assert.notNull(subscribedQueue.name(), "Cannot initialize a subscribe tunnel config with a queue without a name!");
		
		queue = subscribedQueue;
	}
	
	private Integer prefetchCount;
	
	@Override
	public void setPrefetchCount(int count) {
		this.prefetchCount = count;	
	}

	@Override
	public Optional<Integer> getPrefetchCount() {
		return Optional.ofNullable(prefetchCount);
	}
	
	public IAMQPQueue queue() {
		return queue;
	}
}
