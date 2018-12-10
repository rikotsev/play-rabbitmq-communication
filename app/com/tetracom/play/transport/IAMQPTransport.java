package com.tetracom.play.transport;

import com.tetracom.play.transport.actions.IAMQPActions;
import com.tetracom.play.transport.execution.IAMQPExecutor;
import com.tetracom.play.transport.execution.IAMQPTunnelConfigs;

/**
 * The facade to all actions connected with AMQP transport and communication
 * Everything must be initialized, executed and provided from/to here
 * @author radoslav
 *
 */
public interface IAMQPTransport {
	IAMQPExecutor executor();
	IAMQPActions actions();
	IAMQPTunnelConfigs configs();
}
