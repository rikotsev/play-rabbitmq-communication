package com.tetracom.play.transport.execution;

import com.google.inject.AbstractModule;
import com.tetracom.play.transport.config.ConfigModule;

import play.libs.akka.AkkaGuiceSupport;

public class ExecutionModule extends AbstractModule implements AkkaGuiceSupport {

	@Override
	protected void configure() {
		
		install(new ConfigModule());
		
		bindActor(AMQPPublishSupervisor.class, "publish-supervisor");
		bindActor(AMQPSubscribeSupervisor.class, "subscribe-supervisor");
		
		bindActorFactory(AMQPPublishTunnel.class, SupervisorTunnelProtocol.PublishFactory.class);
		bindActorFactory(AMQPSubscribeTunnel.class, SupervisorTunnelProtocol.SubscribeFactory.class);
		
		bind(IAMQPExecutor.class).to(AMQPExecutor.class);
		bind(IAMQPTunnelConfigs.class).to(AMQPTunnelConfigs.class);
		
	}

}
