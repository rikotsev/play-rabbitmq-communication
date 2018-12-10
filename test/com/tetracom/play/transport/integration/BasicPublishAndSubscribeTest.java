package com.tetracom.play.transport.integration;

import play.Application;
import play.Logger;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;


import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.tetracom.play.transport.IAMQPTransport;
import com.tetracom.play.transport.TransportModule;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.actions.IAMQPSubscribeAction;
import com.tetracom.play.transport.actions.IDeliveryHandler;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPQueue;
import com.tetracom.play.transport.execution.IAMQPPublishTunnelConfig;
import com.tetracom.play.transport.execution.IAMQPSubscribeTunnelConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static play.inject.Bindings.bind;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class BasicPublishAndSubscribeTest extends WithApplication {

	private static String result = "";

	private static String result2 = "";

	protected Application provideApplication() {

		Config extraConfig = ConfigFactory
				.parseMap(ImmutableMap.of("rabbitmq.host", "localhost", "rabbitmq.port", "5672", "rabbitmq.virtualHost",
						"law-test", "rabbitmq.username", "radoslav", "rabbitmq.password", "rado123"));

		return new GuiceApplicationBuilder().in(Mode.TEST).bindings(new TransportModule())
				.bindings(bind(IAMQPComponentsProvider.class).to(TestComponentsProvider.class)).configure(extraConfig)
				.build();
	}

	@Test
	public void basicPublishAndSubscribe() {

		Logger.info(
				"================================== Testing basic publish and subscribe ===================================");

		final String message = "TEST";

		final IAMQPTransport transport = app.injector().instanceOf(IAMQPTransport.class);

		final IAMQPPublishTunnelConfig pubConf = transport.configs()
				.publish(TestComponentsProvider.Exchanges.TEST_EXCHANGE_1, TestComponentsProvider.Queues.TEST_QUEUE_1);
		transport.executor().execute(pubConf);

		final IAMQPSubscribeTunnelConfig subConf = transport.configs()
				.subscribe(TestComponentsProvider.Queues.TEST_QUEUE_1);
		transport.executor().execute(subConf);

		final IAMQPPublishAction pubAction = transport.actions()
				.publish(TestComponentsProvider.Exchanges.TEST_EXCHANGE_1, TestComponentsProvider.Queues.TEST_QUEUE_1);
		pubAction.message(message.getBytes(StandardCharsets.UTF_8));

		pubAction.noChannelHandler((act) -> {
			Logger.error("Action failed - no channel");
		});

		transport.executor().execute(pubAction);

		final IAMQPSubscribeAction subAction = transport.actions().subscribe(TestComponentsProvider.Queues.TEST_QUEUE_1,
				new IDeliveryHandler() {

					@Override
					public boolean handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
							byte[] body) throws Exception {

						BasicPublishAndSubscribeTest.result = new String(body, StandardCharsets.UTF_8);

						return true;

					}
				});

		transport.executor().execute(subAction);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicPublishAndSubscribeTest.result, equalTo(message));

	}

	@Test
	public void testReturnListener() {

		Logger.info(
				"================================= Testing return listener ===========================================");

		final String message = "TEST";

		final IAMQPTransport transport = app.injector().instanceOf(IAMQPTransport.class);

		final IAMQPQueue wrongQueue = TestComponentsProvider.wrongQueue();
		// final IAMQPExchange wrongExchnage = TestComponentsProvider.wrongExchange();

		final IAMQPPublishTunnelConfig pubConf = transport.configs()
				.publish(TestComponentsProvider.Exchanges.TEST_EXCHANGE_1, wrongQueue);

		final IAMQPPublishAction pubAct = transport.actions().publish(TestComponentsProvider.Exchanges.TEST_EXCHANGE_1,
				wrongQueue);

		pubAct.message(message.getBytes(StandardCharsets.UTF_8));

		pubConf.setReturnListener(new ReturnListener() {

			@Override
			public void handleReturn(int replyCode, String replyText, String exchange, String routingKey,
					BasicProperties properties, byte[] body) throws IOException {

				BasicPublishAndSubscribeTest.result2 = new String(body, StandardCharsets.UTF_8);

			}
		});

		transport.executor().execute(pubConf);
		transport.executor().execute(pubAct);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicPublishAndSubscribeTest.result2, equalTo(message));

	}

}
