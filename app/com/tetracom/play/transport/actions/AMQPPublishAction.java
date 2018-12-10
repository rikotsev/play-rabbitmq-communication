package com.tetracom.play.transport.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPPublishAction implements IAMQPPublishAction {
	
	private IAMQPExchange exchange;
	
	private IAMQPQueue queue;
	
	private List<byte[]> messages = new ArrayList<>();
	
	private IAMQPQueue replyTo;
	
	private INoChannelHandler noChannelHandler;
	
	public AMQPPublishAction(final IAMQPExchange exchange) {
		this.exchange = exchange;
	}
	
	public AMQPPublishAction(final IAMQPExchange exchange, final IAMQPQueue queue) {
		this.exchange = exchange;
		this.queue = queue;
	}
	
	@Override
	public Optional<IAMQPQueue> queue() {
		return Optional.ofNullable(queue);
	}

	@Override
	public IAMQPExchange exchange() {
		return exchange;
	}

	@Override
	public IAMQPPublishAction messages(Collection<byte[]> messages) {	
		messages.addAll(messages);
		return this;
	}

	@Override
	public IAMQPPublishAction message(byte[] message) {
		messages.add(message);
		return this;
	}

	@Override
	public IAMQPPublishAction replyTo(IAMQPQueue queue) {
		replyTo = queue;
		return this;
	}

	@Override
	public IAMQPPublishAction noChannelHandler(INoChannelHandler handler) {
		noChannelHandler = handler;
		return this;
	}

	@Override
	public Optional<IAMQPQueue> replyTo() {
		return Optional.ofNullable(replyTo);
	}

	@Override
	public Optional<INoChannelHandler> noChannelHandler() {
		return Optional.ofNullable(noChannelHandler);
	}

	@Override
	public Collection<byte[]> messages() {
		return messages;
	}

}
