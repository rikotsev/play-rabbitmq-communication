package com.tetracom.play.transport.actions;

public interface INoChannelHandler {
	void handle(final IAMQPPublishAction failedAction);
}
