package com.tetracom.play.transport.config;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.typesafe.config.Config;

import play.Logger;

/**
 * 
 * 
 * @author radoslav
 *
 */
@Singleton
class AMQPConfig implements IAMQPConfig {

	private static int MAX_CONNECTION_ATTEMPTS = 5;

	private static String CONFIG_KEY_HOST = "rabbitmq.host";
	private static String CONFIG_KEY_PORT = "rabbitmq.port";
	private static String CONFIG_KEY_VIRTUAL_HOST = "rabbitmq.virtualHost";
	private static String CONFIG_KEY_USERNAME = "rabbitmq.username";
	private static String CONFIG_KEY_PASSWORD = "rabbitmq.password";

	private Config config;

	private ConnectionFactory connectionFactory;

	@Inject
	public AMQPConfig(final Config config) {
		this.config = config;
	}

	@Override
	public Connection getConnection() throws IOException, TimeoutException, InterruptedException {
		
		for(int i = 1; i <= MAX_CONNECTION_ATTEMPTS; i++) {
			try {
				checkFactory(); //The config might have changed, we read it again
				final Connection conn = connectionFactory.newConnection();
				
				Logger.info("AMQP >> Connection with standard threadpool established with \n {}:{}/{} using username {}", host(), port(), virtualHost(), username() );
				
				return conn;
			}
			catch(final Exception e) {
				if( i == MAX_CONNECTION_ATTEMPTS) {
					throw e;
				}
				else {
					Logger.warn("AMQP >> Failed to establish connection! Retrying... attempt # {} ", i, e);
					Thread.sleep(500);
				}
			}
		}
		
		throw new RuntimeException("Failed to get a connection to message broker!");
	}

	@Override
	public Connection getConnection(ExecutorService executorService)
			throws TimeoutException, IOException, InterruptedException {
		for(int i = 1; i <= MAX_CONNECTION_ATTEMPTS; i++) {
			try {
				checkFactory(); //The config might have changed, we read it again
				final Connection conn = connectionFactory.newConnection(executorService);
				
				Logger.info("AMQP >> Connection with custom threadpool established with \n {}:{}/{} using username {}", host(), port(), virtualHost(), username() );
				
				return conn;
			}
			catch(final Exception e) {
				if( i == MAX_CONNECTION_ATTEMPTS) {
					throw e;
				}
				else {
					Logger.warn("AMQP >> Failed to establish connection! Retrying... attempt # {} ", i, e);
					Thread.sleep(500);
				}
			}
		}
		
		throw new RuntimeException("Failed to get a connection to message broker!");
	}

	private String host() {
		return config.getString(CONFIG_KEY_HOST);
	}

	private int port() {
		return config.getInt(CONFIG_KEY_PORT);
	}

	private String virtualHost() {
		return config.getString(CONFIG_KEY_VIRTUAL_HOST);
	}

	private String username() {
		return config.getString(CONFIG_KEY_USERNAME);
	}

	private String password() {
		return config.getString(CONFIG_KEY_PASSWORD);
	}

	private synchronized void checkFactory() {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
			connectionFactory.setHost(host());
			connectionFactory.setPort(port());
			connectionFactory.setVirtualHost(virtualHost());
			connectionFactory.setUsername(username());
			connectionFactory.setPassword(password());
			connectionFactory.setAutomaticRecoveryEnabled(true);
		}
	}

}
