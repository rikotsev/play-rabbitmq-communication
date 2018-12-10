package com.tetracom.play.transport.execution;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.rabbitmq.client.Connection;
import com.tetracom.play.transport.IAMQPPublishBase;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.config.IAMQPConfig;

import akka.actor.ActorRef;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.akka.InjectedActorSupport;

@Singleton
class AMQPPublishSupervisor extends AAMQPConnectionHandler implements InjectedActorSupport {

	private static final String ACTOR_NAME_POSTFIX = "PUBLISHER";

	private final SupervisorTunnelProtocol.PublishFactory publishFactory;

	private HashMap<String, ActorRef> publishTunnels = new HashMap<>();

	@Inject
	public AMQPPublishSupervisor(ApplicationLifecycle lifecycle, IAMQPConfig config,
			SupervisorTunnelProtocol.PublishFactory publishFactory) {
		super(lifecycle, config);
		this.publishFactory = publishFactory;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(IAMQPPublishTunnelConfig.class, (config) -> {

			final String key = keyValue(config);

			if (!publishTunnels.containsKey(key)) {

				final Connection conn = getConnection();

				final ActorRef publishTunnel = injectedChild(
						() -> publishFactory.create(conn, IAMQPPublishTunnelConfig.class, IAMQPPublishAction.class),
						key + "_" + ACTOR_NAME_POSTFIX);

				publishTunnels.put(key, publishTunnel);
			}

			publishTunnels.get(key).tell(config, getSelf());

		}).match(IAMQPPublishAction.class, (action) -> {

			final String key = keyValue(action);

			if (publishTunnels.containsKey(key)) {
				publishTunnels.get(key).tell(action, getSelf());
			} else {
				Logger.error("AMQP >> Attempting to publish through a not configured tunnel!");
				throw new RuntimeException(
						"Configure the tunnel for " + key + " before trying to publish on it first!");
			}

		}).matchEquals(KEEP_ALIVE_MSG, (ka) -> {
			final Connection conn = getConnection();

			publishTunnels.values().stream().forEach(actor -> {
				actor.tell(new SupervisorTunnelProtocol.KeepAlive(conn), getSelf());
			});

			context().system().scheduler().scheduleOnce(KEEP_ALIVE_INTERVAL, getSelf(), KEEP_ALIVE_MSG,
					context().dispatcher(), ActorRef.noSender());
		}).build();
	}

	@Override
	public void preStart() {
		getSelf().tell(KEEP_ALIVE_MSG, ActorRef.noSender());
	}

	private String keyValue(final IAMQPPublishBase basicPublish) {
		if (basicPublish.queue().isPresent()) {
			return String.format("%s_%s", basicPublish.exchange().name(), basicPublish.queue().get().name());
		} else {
			return basicPublish.exchange().name();
		}
	}

}
