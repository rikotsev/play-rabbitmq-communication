package com.tetracom.play.transport.actions;

import java.util.Map;

import org.junit.Test;

import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.tetracom.play.transport.config.IAMQPQueue;

public class AMQPSubscribeActionTest {
	
	@Test
	public void properData() {
		actions().subscribe(new IAMQPQueue() {

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
				// TODO Auto-generated method stub
				return "TEST_KEY";
			}

			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return "TEST_NAME";
			}
			
		}, new IDeliveryHandler() {
			
			@Override
			public boolean handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueue() {
		actions().subscribe(null, new IDeliveryHandler() {
			
			@Override
			public boolean handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullQueueName() {
		actions().subscribe(new IAMQPQueue() {

			@Override
			public String name() {
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
				return "TEST_KEY";
			}

			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return "TEST_NAME";
			}
			
		}, new IDeliveryHandler() {
			
			@Override
			public boolean handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullHandler() {
		actions().subscribe(new IAMQPQueue() {

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
				// TODO Auto-generated method stub
				return "TEST_KEY";
			}

			@Override
			public String exchangeName() {
				// TODO Auto-generated method stub
				return "TEST_NAME";
			}
			
		}, null);
	}
	
	private IAMQPActions actions() {
		return new AMQPActions();
	}
}
