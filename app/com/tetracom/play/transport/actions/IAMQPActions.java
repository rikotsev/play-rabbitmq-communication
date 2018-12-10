package com.tetracom.play.transport.actions;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;
import com.tetracom.play.transport.execution.IAMQPExecutor;

/**
 * Is a factory for actions that have to be supplied to the
 * {@link IAMQPExecutor}
 * 
 * @author radoslav
 *
 */
public interface IAMQPActions {
	
	IAMQPPublishAction publish(final IAMQPExchange exchange);
	IAMQPPublishAction publish(final IAMQPExchange exchange, final IAMQPQueue queue);
	IAMQPSubscribeAction subscribe(final IAMQPQueue queue, final IDeliveryHandler handler);
	
}
