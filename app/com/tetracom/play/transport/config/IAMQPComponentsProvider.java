package com.tetracom.play.transport.config;

import java.util.Collection;

/**
 * Provides all the exchanges and queues we want to have in the current
 * communication scheme
 * 
 * @author radoslav
 *
 */
public interface IAMQPComponentsProvider {
	
	/**
	 * The queues that will be declared and binded to exchanges
	 * @return
	 */
	Collection<IAMQPQueue> provideQueues();
	
	/**
	 * The exchanges that will be declared
	 * @return
	 */
	Collection<IAMQPExchange> provideExchanges();

}
