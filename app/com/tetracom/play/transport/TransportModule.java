package com.tetracom.play.transport;

import com.google.inject.AbstractModule;
import com.tetracom.play.transport.actions.ActionsModule;
import com.tetracom.play.transport.execution.ExecutionModule;

public class TransportModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ActionsModule());
		install(new ExecutionModule());
		
		bind(IAMQPTransport.class).to(AMQPTransport.class);
		
	}

}
