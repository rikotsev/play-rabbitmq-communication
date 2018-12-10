package com.tetracom.play.transport.config;

import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IAMQPConfig.class).to(AMQPConfig.class);
		bind(IAMQPInitializer.class).to(AMQPInitializer.class);
	}

}
