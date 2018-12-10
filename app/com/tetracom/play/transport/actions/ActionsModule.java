package com.tetracom.play.transport.actions;

import com.google.inject.AbstractModule;

public class ActionsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IAMQPActions.class).to(AMQPActions.class);
	}

}
