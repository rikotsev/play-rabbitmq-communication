package com.tetracom.play.transport.execution;

import java.util.Optional;

import com.tetracom.play.transport.IAMQPSubscribeBase;

public interface IAMQPSubscribeTunnelConfig extends IAMQPSubscribeBase {
	void setPrefetchCount(final int count);
	
	Optional<Integer> getPrefetchCount();
}
