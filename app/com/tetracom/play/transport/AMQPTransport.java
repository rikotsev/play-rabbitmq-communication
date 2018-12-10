package com.tetracom.play.transport;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tetracom.play.transport.actions.IAMQPActions;
import com.tetracom.play.transport.execution.IAMQPExecutor;
import com.tetracom.play.transport.execution.IAMQPTunnelConfigs;

@Singleton
class AMQPTransport implements IAMQPTransport {
	
	private IAMQPActions actions;
	
	private IAMQPExecutor executor;
	
	private IAMQPTunnelConfigs configs;
	
	@Inject
	public AMQPTransport(final IAMQPActions actions, final IAMQPExecutor executor, final IAMQPTunnelConfigs configs) {
		this.actions = actions;
		this.executor = executor;
		this.configs = configs;
	}
	
	@Override
	public IAMQPExecutor executor() {
		return executor;
	}

	@Override
	public IAMQPActions actions() {
		return actions;
	}

	@Override
	public IAMQPTunnelConfigs configs() {
		return configs;
	}

}
