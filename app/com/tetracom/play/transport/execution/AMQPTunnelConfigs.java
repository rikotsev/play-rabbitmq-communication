package com.tetracom.play.transport.execution;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

class AMQPTunnelConfigs implements IAMQPTunnelConfigs {

	@Override
	public IAMQPPublishTunnelConfig publish(IAMQPExchange exchange) {
		return new AMQPPublishTunnelConfig(exchange);
	}

	@Override
	public IAMQPPublishTunnelConfig publish(IAMQPExchange exchange, IAMQPQueue queue) {
		return new AMQPPublishTunnelConfig(exchange, queue);
	}

	@Override
	public IAMQPSubscribeTunnelConfig subscribe(IAMQPQueue queue) {
		return new AMQPSubsribeTunnelConfig(queue);
	}
	
}
