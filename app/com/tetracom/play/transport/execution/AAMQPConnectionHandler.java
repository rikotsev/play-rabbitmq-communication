package com.tetracom.play.transport.execution;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.tetracom.play.transport.config.IAMQPConfig;

import akka.actor.AbstractActor;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.akka.InjectedActorSupport;
import scala.concurrent.duration.FiniteDuration;

abstract class AAMQPConnectionHandler extends AbstractActor implements InjectedActorSupport {
	
	protected static final String KEEP_ALIVE_MSG = "KeepAlive";
	
	protected static final FiniteDuration KEEP_ALIVE_INTERVAL = new FiniteDuration(1, TimeUnit.MINUTES);
	
	private Connection connection;

	private IAMQPConfig config;

	private ExecutorService executor;

	public AAMQPConnectionHandler(final ApplicationLifecycle lifecycle, final IAMQPConfig config) {
		
		this.config = config;
		
		lifecycle.addStopHook(() -> {

			terminate();

			return CompletableFuture.completedFuture(null);
		});
	}

	final protected Connection getConnection() {
		try {
			if (connection == null) {
				initialize();
			}
			else if (!connection.isOpen()) {
	
				terminate();
				initialize();
			}
		}
		catch(final TimeoutException | IOException | InterruptedException ex) {
			Logger.error("AMQP >> Failed to get a connection for the connection handler");
		}
		
		return connection;
	}

	final private void initialize() throws TimeoutException, IOException, InterruptedException {

		if (threadCount().isPresent()) {
			executor = Executors.newFixedThreadPool(threadCount().get());
			connection = config.getConnection(executor);
		}
		else {
			connection = config.getConnection();
		}
		

	}

	/**
	 * Terminates the previous connections and prints out the error
	 */
	final private void terminate() {
		try {
			connection.close();
		} catch (final Exception e) {
			Logger.error("AMQP >> There was an issue with the connection", e);
		} finally {
			connection.abort();
			if (executor != null) {
				executor.shutdown();
			}
		}
	}

	protected Optional<Integer> threadCount() {
		return Optional.empty();
	}

}
