package com.tetracom.play.transport.actions;

import java.util.Collection;
import java.util.Optional;

import com.tetracom.play.transport.IAMQPPublishBase;
import com.tetracom.play.transport.config.IAMQPQueue;


/**
 * A wrap around the publish action
 * @author radoslav
 *
 */
public interface IAMQPPublishAction extends IAMQPPublishBase {
	
	IAMQPPublishAction messages(final Collection<byte[]> messages);
	IAMQPPublishAction message(final byte[] message);
	IAMQPPublishAction replyTo(final IAMQPQueue queue);
	IAMQPPublishAction noChannelHandler(final INoChannelHandler handler);
	Optional<IAMQPQueue> replyTo();
	Optional<INoChannelHandler> noChannelHandler();
	Collection<byte[]> messages();
	
}
