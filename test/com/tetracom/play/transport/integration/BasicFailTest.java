package com.tetracom.play.transport.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.inject.Bindings.bind;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.tetracom.play.transport.IAMQPTransport;
import com.tetracom.play.transport.TransportModule;
import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.execution.IAMQPPublishTunnelConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Application;
import play.Logger;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

public class BasicFailTest extends WithApplication {
	
	private static String result = "";
	
	protected Application provideApplication() {

		Config extraConfig = ConfigFactory
				.parseMap(ImmutableMap.of("rabbitmq.host", "localhost", "rabbitmq.port", "5672", "rabbitmq.virtualHost",
						"law-test", "rabbitmq.username", "radoslav", "rabbitmq.password", "rado1234")); //Wrong password

		return new GuiceApplicationBuilder().in(Mode.TEST).bindings(new TransportModule())
				.bindings(bind(IAMQPComponentsProvider.class).to(TestComponentsProvider.class)).configure(extraConfig)
				.build();
	}
	
	@Test
	public void basicConfigFail() {
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
			BasicFailTest.result = new String(fail.messages().stream().findAny().get(),
					StandardCharsets.UTF_8);

		});

		transport.executor().execute(pubConf);
		transport.executor().execute(pubAct1);
		
		transport.executor().execute(pubAct2);

		try {
			Thread.sleep(10000);
		} catch (final Exception e) {

		}

		assertThat(BasicFailTest.result, equalTo(message2));
	}
	
}
