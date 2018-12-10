package com.tetracom.play.transport.execution;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.rabbitmq.client.Connection;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;
import com.tetracom.play.transport.config.IAMQPConfig;
import com.tetracom.play.transport.config.IAMQPQueue;

import akka.actor.ActorRef;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.akka.InjectedActorSupport;

@Singleton
class AMQPSubscribeSupervisor extends AAMQPConnectionHandler implements InjectedActorSupport {

	private static String ACTOR_NAME_POSTFIX = "SUBSCRIBER";

	private SupervisorTunnelProtocol.SubscribeFactory subscribeFactory;

	

	private HashMap<IAMQPQueue, ActorRef> subscribeTunnels = new HashMap<>();

	@Inject
	public AMQPSubscribeSupervisor(ApplicationLifecycle lifecycle, IAMQPConfig config,
			SupervisorTunnelProtocol.SubscribeFactory subscribeFactory) {
		super(lifecycle, config);

		this.subscribeFactory = subscribeFactory;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(IAMQPSubscribeTunnelConfig.class, (config) -> {

			final IAMQPQueue queue = config.queue();

			if (!subscribeTunnels.containsKey(queue)) {

				final Connection conn = getConnection();

				final ActorRef subscribeTunnel = injectedChild(() -> subscribeFactory.create(conn,
						IAMQPSubscribeTunnelConfig.class, IAMQPSubscribeAction.class),
						queue.name() + "_" + ACTOR_NAME_POSTFIX);

				subscribeTunnels.put(queue, subscribeTunnel);
			}

			subscribeTunnels.get(queue).tell(config, getSelf());

		}).match(IAMQPSubscribeAction.class, (action) -> {

			final IAMQPQueue queue = action.queue();

			if (subscribeTunnels.containsKey(queue)) {
				subscribeTunnels.get(queue).tell(action, getSelf());
			} else {
				Logger.error("AMQP >> Attempting to subscribe to a not configured tunnel!");
				throw new RuntimeException(
						"Configure the tunnel for queue " + queue.name() + "before trying to subscribe!");
			}

		}).matchEquals(KEEP_ALIVE_MSG, (ka) -> {

			final Connection conn = getConnection();

			subscribeTunnels.values().stream().forEach(actor -> {
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

}
