package com.tetracom.play.transport.integration;

import play.Application;
import play.Logger;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.test.WithApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;
import com.tetracom.play.transport.execution.IAMQPPublishTunnelConfig;
import com.tetracom.play.transport.execution.IAMQPSubscribeTunnelConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static play.inject.Bindings.bind;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

public class BasicPublishAndSubscribe extends WithApplication {

	private static String API_HOST = "http://localhost:15672/api/";

	private static String result = "";

	private static String result2 = "";

	private static String result3 = "";

	protected Application provideApplication() {

		Config extraConfig = ConfigFactory
				.parseMap(ImmutableMap.of("rabbitmq.host", "localhost", "rabbitmq.port", "5672", "rabbitmq.virtualHost",
						"law-test", "rabbitmq.username", "radoslav", "rabbitmq.password", "rado123"));

		return new GuiceApplicationBuilder().in(Mode.TEST).bindings(new TransportModule())
				.bindings(bind(IAMQPComponentsProvider.class).to(TestComponentsProvider.class)).configure(extraConfig)
				.build();
	}

	@Test
	public void basicPublishAndSubscribe1on1() {

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

						BasicPublishAndSubscribe.result = new String(body, StandardCharsets.UTF_8);

						return true;

					}
				});

		transport.executor().execute(subAction);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicPublishAndSubscribe.result, equalTo(message));

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

				BasicPublishAndSubscribe.result2 = new String(body, StandardCharsets.UTF_8);

			}
		});

		transport.executor().execute(pubConf);
		transport.executor().execute(pubAct);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicPublishAndSubscribe.result2, equalTo(message));

	}

	@Test
	public void testNoChannelHandler() {

		Logger.info(
				"================================= Testing sudden connection drop ===========================================");

		final String message = "TEST";
		final String message2 = "TEST2";

		final IAMQPTransport transport = app.injector().instanceOf(IAMQPTransport.class);

		//final IAMQPExchange wrongExchange = TestComponentsProvider.wrongExchange();
		final IAMQPExchange wrongExchange = TestComponentsProvider.Exchanges.TEST_EXCHANGE_2;

		final IAMQPPublishTunnelConfig pubConf = transport.configs().publish(wrongExchange);
		final IAMQPPublishAction pubAct1 = transport.actions().publish(wrongExchange);
		final IAMQPPublishAction pubAct2 = transport.actions().publish(wrongExchange);

		pubAct1.message(message.getBytes(StandardCharsets.UTF_8));
		pubAct2.message(message2.getBytes(StandardCharsets.UTF_8));
		pubAct2.noChannelHandler((fail) -> {

			Logger.debug("In no channel handler!");
			BasicPublishAndSubscribe.result3 = new String(fail.messages().stream().findAny().get(),
					StandardCharsets.UTF_8);

		});

		transport.executor().execute(pubConf);
		transport.executor().execute(pubAct1);

		//deleteAllConnections();
		keepConnectionsClosed();
		
		transport.executor().execute(pubAct2);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicPublishAndSubscribe.result3, equalTo(message2));
	}

	private static boolean noConnections = true;

	private void deleteAllConnections() {
		final WSClient ws = app.injector().instanceOf(WSClient.class);

		while (noConnections) {

			ws.url(API_HOST + "connections").setAuth("radoslav", "rado123", WSAuthScheme.BASIC).get()
					.thenApply(response -> response.asJson())
					.whenComplete((js, ex) -> {

						final ArrayNode names = (ArrayNode) js;
						
						Logger.info("names = {}", js);
						
						if (names.size() > 0) {

							for (final JsonNode obj : names) {
								try {
									ws.url(API_HOST + "connections/" + URLEncoder.encode(obj.get("name").asText(), "UTF-8")).delete();
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Logger.debug("Deleting connection " + obj.get("name").asText());
							}

							noConnections = false;
						}

					});
			
			try {
				Thread.sleep(500);
			} catch (final Exception e) {

			}

		}

	}
	
	private void keepConnectionsClosed() {
		
		final WSClient ws = app.injector().instanceOf(WSClient.class);
		
		final Runnable thread = new Runnable() {

			@Override
			public void run() {
				
				while(true) {
					
					ws.url(API_HOST + "connections").setAuth("radoslav", "rado123", WSAuthScheme.BASIC).get()
					.thenApply(response -> response.asJson())
					.whenComplete((js, ex) -> {

						final ArrayNode names = (ArrayNode) js;
						
						Logger.info("names = {}", js);
						
						if (names.size() > 0) {

							for (final JsonNode obj : names) {
								try {
									final String delUrl = new URIBuilder(API_HOST + "connections/" + obj.get("name").asText()).toString();
									Logger.debug("url = {}", delUrl);
									ws.url(delUrl).delete();
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								Logger.debug("Deleting connection " + obj.get("name").asText());
							}

						}

					});
					
					
				}
					
			}
			
		};
		
		new Thread(thread).start();
	}

}
