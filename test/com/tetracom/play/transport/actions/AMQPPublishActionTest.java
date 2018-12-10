package com.tetracom.play.transport.actions;

import org.junit.Test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.tetracom.play.transport.config.IAMQPExchange;

public class AMQPPublishActionTest {
	
	@Test
	public void properData() {
		final IAMQPPublishAction pubAction = actions().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return "TEST_EXCHANGE";
			}
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullExchange() {
		final IAMQPPublishAction pubAction = actions().publish(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullNameExchange() {
		final IAMQPPublishAction pubAction = actions().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return null;
			}
		});
	}
	
	@Test
	public void nullQueueRoutingKey() {
		final IAMQPPublishAction pubAction = actions().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return null;
			}
		});
	}
	
	private IAMQPActions actions() {
		return new AMQPActions();
	}
}
