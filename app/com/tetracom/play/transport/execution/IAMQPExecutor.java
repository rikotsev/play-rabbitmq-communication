package com.tetracom.play.transport.execution;

import com.tetracom.play.transport.actions.IAMQPActions;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;

/**
 * Executes all actions (either publish or subscribe). Actions can be acquired
 * from {@link IAMQPActions}
 * 
 * @author radoslav
 *
 */
public interface IAMQPExecutor {
	void execute(IAMQPPublishTunnelConfig config);
	void execute(IAMQPPublishAction action);
	void execute(IAMQPSubscribeTunnelConfig config);
	void execute(IAMQPSubscribeAction action);
}
