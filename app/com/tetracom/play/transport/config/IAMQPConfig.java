package com.tetracom.play.transport.config;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;

/**
 * Provides a connection to the AMQP broker
 * @author radoslav
 *
 */
public interface IAMQPConfig {
	/**
	 * The connection is with an internally implemented thread pool of the broker client
	 * @return
	 * @throws IOException - if there is a problem while getting the connection
	 * @throws TimeoutException
	 * @throws InterruptedException - if the thread is interrupted while retrying to get a connection
	 */
	Connection getConnection() throws IOException, TimeoutException, InterruptedException;
	/**
	 * The connection is with a thread pool we pass
	 * @param executorService
	 * @return
	 * @throws TimeoutException
	 * @throws IOException - if there is a problem while getting the connection
	 * @throws InterruptedException - if the thread is unterrupted while retrying to get a connection
	 */
	Connection getConnection(final ExecutorService executorService) throws TimeoutException, IOException, InterruptedException;
}
