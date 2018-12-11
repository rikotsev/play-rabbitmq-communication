package com.tetracom.play.transport.actions;

import java.util.Map;

import org.junit.Test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

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
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueueRoutingKey() {
		final IAMQPPublishAction pubAction = actions().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return "TEST_EXCHANGE";
			}
		}, new IAMQPQueue() {
			
			@Override
			public String routingKey() {
				return null;
			}
			
			@Override
			public String name() {
				return "TEST_QUEUE";
			}
			
			@Override
			public boolean exclusive() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean durable() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean autoDelete() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> arguments() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueueName() {
		final IAMQPPublishAction pubAction = actions().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return "TEST_EXCHANGE";
			}
		}, new IAMQPQueue() {
			
			@Override
			public String routingKey() {
				return "TEST_ROUTING_KEY";
			}
			
			@Override
			public String name() {
				return null;
			}
			
			@Override
			public boolean exclusive() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean durable() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean autoDelete() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> arguments() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	private IAMQPActions actions() {
		return new AMQPActions();
	}
}
