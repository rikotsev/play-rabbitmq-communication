package com.tetracom.play.transport.actions;

import java.util.Optional;

import com.tetracom.play.transport.IAMQPSubscribeBase;

public interface IAMQPSubscribeAction extends IAMQPSubscribeBase {
	IAMQPSubscribeAction setAutoAck(final boolean autoAck);
	IDeliveryHandler getDeliveryHandler();
	Optional<Boolean> getAutoAck();
}
