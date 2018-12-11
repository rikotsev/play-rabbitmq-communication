package com.tetracom.play.transport.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.util.Assert;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;


class AMQPPublishAction implements IAMQPPublishAction {
	
	private IAMQPExchange exchange;
	
	private IAMQPQueue queue;
	
	private List<byte[]> messages = new ArrayList<>();
	
	private IAMQPQueue replyTo;
	
	private INoChannelHandler noChannelHandler;
	
	public AMQPPublishAction(final IAMQPExchange exchange) {
		//Needed in order for the publish supervisor to redirect and for actions in the Publish Tunnel
		Assert.notNull(exchange,"A publish action without an exchange cannot be initialized!");
		Assert.notNull(exchange.name(), "A publish action for an exchange without name cannot be initialized!");
		
		this.exchange = exchange;
	}
	
	public AMQPPublishAction(final IAMQPExchange exchange, final IAMQPQueue queue) {
		this(exchange);
		
		if(queue != null) {
			//Needed in order for the publish supervisor to redirect
			Assert.notNull(queue.name(), "Cannot publish to a queue via exchange with the queue having no name!");
			//Needed for actions in the publish tunnel
			Assert.notNull(queue.routingKey(), "Cannot publish to a queue via exchange without a routing key for the queue!");
		}
		
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
