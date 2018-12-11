package com.tetracom.play.transport.execution;

import java.util.Map;

import org.junit.Test;

import com.tetracom.play.transport.config.IAMQPQueue;

public class AMQPSubscribeTunnelConfigTest {
	
	@Test
	public void properData() {
		configs().subscribe(new IAMQPQueue() {

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
				return "TEST_EXCHANGE";
			}
			
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueue() {
		configs().subscribe(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueueName() {
		configs().subscribe(new IAMQPQueue() {

			@Override
			public String name() {
				// TODO Auto-generated method stub
				return null;
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
				// TODO Auto-generated method stub
				return "TEST_ROUTING_KEY";
			}

			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return "TEST_EXCHANGE";
			}
			
		});
	}
	
	private IAMQPTunnelConfigs configs() {
		return new AMQPTunnelConfigs();
	}
}
