package com.tetracom.play.transport.execution;

import java.util.Optional;

import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPSubsribeTunnelConfig implements IAMQPSubscribeTunnelConfig {
	
	private IAMQPQueue queue;
	
	public AMQPSubsribeTunnelConfig(final IAMQPQueue subscribedQueue) {
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
