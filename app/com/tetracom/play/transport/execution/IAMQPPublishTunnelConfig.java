package com.tetracom.play.transport.execution;

import java.util.Optional;

import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.ReturnListener;
import com.tetracom.play.transport.IAMQPPublishBase;

public interface IAMQPPublishTunnelConfig extends IAMQPPublishBase {
	void setConfirmListener(final ConfirmListener confirmListener);
	void setReturnListener(final ReturnListener returnListener);
	
	Optional<ConfirmListener> getConfirmListener();
	Optional<ReturnListener> getReturnListener();
}
