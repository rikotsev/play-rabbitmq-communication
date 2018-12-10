package com.tetracom.play.transport.config;

import com.rabbitmq.client.Channel;

/**
 * Binds all the provided components so that if the channel uses any of them
 * they will exist
 * 
 * @author radoslav
 *
 */
public interface IAMQPInitializer {
	
	void initialize(final Channel channel, final IAMQPComponentsProvider componentsProvider);

}
