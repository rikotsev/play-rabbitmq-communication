package com.tetracom.play.transport.execution;

import java.util.Map;

import org.junit.Test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

public class AMQPPublishTunnelConfigTest {
	
	@Test
	public void properData() {
		configs().publish(new IAMQPExchange() {
			
			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
			@Override
			public String name() {
				return "EXCHANGE_NAME";
			}
		});
	}
	
	@Test
	public void properData2() {
		configs().publish(new IAMQPExchange() {

			@Override
			public String name() {
				return "EXCHANGE_NAME";
			}

			@Override
			public BuiltinExchangeType type() {
				return BuiltinExchangeType.DIRECT;
			}
			
		}, new IAMQPQueue() {

			@Override
			public String name() {
				return "TEST_QUEUE";
			}

			@Override
			public boolean durable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean exclusive() {
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

			@Override
			public String routingKey() {
				return "TEST_ROUTING_KEY";
			}

			@Override
			public String exchangeName() {
				return "TEST_EXCHANGE_NAME";
			}
			
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullExchange() {
		configs().publish(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullExchangeName() {
		configs().publish(new IAMQPExchange() {
			
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
	public void nullQueueName() {
		configs().publish(new IAMQPExchange() {
			
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
			public String name() {
				return null;
			}

			@Override
			public boolean durable() {
				return false;
			}

			@Override
			public boolean exclusive() {
				return false;
			}

			@Override
			public boolean autoDelete() {
				return false;
			}

			@Override
			public Map<String, Object> arguments() {
				return null;
			}

			@Override
			public String routingKey() {
				return "TEST_ROUTING_KEY";
			}

			@Override
			public String exchangeName() {
				return "TEST_EXCHANGE_NAME";
			}
			
		});
	}
	
	private IAMQPTunnelConfigs configs() {
		return new AMQPTunnelConfigs();
	}
}
