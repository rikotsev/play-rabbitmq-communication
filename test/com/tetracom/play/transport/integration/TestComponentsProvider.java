package com.tetracom.play.transport.integration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.rabbitmq.client.BuiltinExchangeType;
import com.tetracom.play.transport.config.IAMQPComponentsProvider;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;


class TestComponentsProvider implements IAMQPComponentsProvider {
	
	static class WrongQueue implements IAMQPQueue {
		
		private String name = UUID.randomUUID().toString();
		
		@Override
		public String name() {
			return name;
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
			return "fail";
		}

		@Override
		public String exchangeName() {
			return "";
		}
		
	}
	
	static class WrongExchange implements IAMQPExchange {
		private String name = UUID.randomUUID().toString();

		@Override
		public String name() {
			return name;
		}

		@Override
		public BuiltinExchangeType type() {
			return BuiltinExchangeType.DIRECT;
		}
		
		
	}
	
	enum Exchanges implements IAMQPExchange {
		TEST_EXCHANGE_1(BuiltinExchangeType.DIRECT), TEST_EXCHANGE_2(BuiltinExchangeType.FANOUT);
		
		private BuiltinExchangeType type;
		
		Exchanges(final BuiltinExchangeType type) {
			this.type = type;
		}
		
		@Override
		public BuiltinExchangeType type() {
			return type;
		}
		
	}
	
	enum Queues implements IAMQPQueue {
		
		TEST_QUEUE_1("test_q_1",Exchanges.TEST_EXCHANGE_1.name()), TEST_QUEUE_2("test_q_2", Exchanges.TEST_EXCHANGE_2.name());
		
		private String routingKey;
		private String exchangeName;
		
		Queues(final String routingKey, final String exchangeName){
			this.routingKey = routingKey;
			this.exchangeName = exchangeName;
		};
		
		

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
			return routingKey;
		}

		@Override
		public String exchangeName() {
			return exchangeName;
		}
		
	}
	
	@Inject
	public TestComponentsProvider() {}
	
	@Override
	public Collection<IAMQPQueue> provideQueues() {
		return Arrays.asList(Queues.values());
	}

	@Override
	public Collection<IAMQPExchange> provideExchanges() {
		return Arrays.asList(Exchanges.values());
	}
	
	public static IAMQPQueue wrongQueue() {
		return new WrongQueue();
	}
	
	public static IAMQPExchange wrongExchange() {
		return new WrongExchange();
	}
	
}
