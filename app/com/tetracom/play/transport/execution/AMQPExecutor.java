package com.tetracom.play.transport.execution;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.util.Assert;

import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;

import akka.actor.ActorRef;

@Singleton
class AMQPExecutor implements IAMQPExecutor {
	
	private ActorRef publishSupervisor;
	
	private ActorRef subscribeSupervisor;
	
	@Inject
	public AMQPExecutor(@Named("publish-supervisor") ActorRef publishSupervisor,
			@Named("subscribe-supervisor") ActorRef subscribeSupervisor) {
		this.publishSupervisor = publishSupervisor;
		this.subscribeSupervisor = subscribeSupervisor;
	}
	
	@Override
	public void execute(final IAMQPPublishTunnelConfig config) {
		publishSupervisor.tell(config, ActorRef.noSender());
	}
	
	@Override
	public void execute(final IAMQPPublishAction action) {
		//It is useless to execute a publish without any messages, better report it so that the code will be reviewed
		Assert.notEmpty(action.messages());
		
		publishSupervisor.tell(action, ActorRef.noSender());
	}
	
	@Override
	public void execute(final IAMQPSubscribeTunnelConfig config) {
		subscribeSupervisor.tell(config, ActorRef.noSender());
	}
	
	@Override
	public void execute(final IAMQPSubscribeAction action) {
		subscribeSupervisor.tell(action, ActorRef.noSender());
	}
}
