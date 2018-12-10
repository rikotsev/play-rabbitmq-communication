package com.tetracom.play.transport.execution;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

public interface IAMQPTunnelConfigs {
	IAMQPPublishTunnelConfig publish(final IAMQPExchange exchange);
	IAMQPPublishTunnelConfig publish(final IAMQPExchange exchange, final IAMQPQueue queue);
	IAMQPSubscribeTunnelConfig subscribe(final IAMQPQueue subscribe);
}
