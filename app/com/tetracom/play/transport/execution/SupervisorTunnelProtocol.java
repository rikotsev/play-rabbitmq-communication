package com.tetracom.play.transport.execution;

import com.rabbitmq.client.Connection;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;

import akka.actor.Actor;

class SupervisorTunnelProtocol {
	
	private SupervisorTunnelProtocol() {}
	
	static class KeepAlive {
		private Connection conn;
		
		public KeepAlive(final Connection healthyConnection) {
			this.conn = healthyConnection;
		}
		
		public Connection getConnection() {
			return conn;
		}
	}
	
	interface SubscribeFactory {
		Actor create(final Connection connection, final Class<IAMQPSubscribeTunnelConfig> configType, final Class<IAMQPSubscribeAction> actionType);
	}
	
	interface PublishFactory {
		Actor create(final Connection connection, final Class<IAMQPPublishTunnelConfig> configType, final Class<IAMQPPublishAction> actionType);
	}
	
}
