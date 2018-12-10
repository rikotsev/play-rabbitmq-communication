package com.tetracom.play.transport.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.rabbitmq.client.BuiltinExchangeType;

public class AMQPInitializerTest {
	
	public void properDataTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return "test";
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "test_exchange";
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidExchangeNameTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return "test";
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return null;
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidExchangeTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return "test";
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				final ArrayList<IAMQPExchange> exchanges = new ArrayList<>();
				exchanges.add(null);
				
				return exchanges;
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidExchangeTypeTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return "test";
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "TEST_EXCHANGE";
					}

					@Override
					public BuiltinExchangeType type() {
						return null;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidQueueTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				final ArrayList<IAMQPQueue> queues = new ArrayList<>();
				queues.add(null);
				
				return queues;
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "TEST_EXCHANGE";
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidQueueNameTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

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
						return "test";
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "TEST_EXCHANGE";
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidQueueExchangeNameTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return "test";
					}

					@Override
					public String exchangeName() {
						return null;
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "TEST_EXCHANGE";
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidQueueRoutingKeyTest() {
		final IAMQPComponentsProvider provider = new IAMQPComponentsProvider() {
			
			@Override
			public Collection<IAMQPQueue> provideQueues() {
				return Arrays.asList(new IAMQPQueue() {

					@Override
					public String name() {
						return "test_queue";
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
						return null;
					}

					@Override
					public String exchangeName() {
						return "test_exchange";
					}
					
				});
			}
			
			@Override
			public Collection<IAMQPExchange> provideExchanges() {
				return Arrays.asList(new IAMQPExchange() {

					@Override
					public String name() {
						return "TEST_EXCHANGE";
					}

					@Override
					public BuiltinExchangeType type() {
						return BuiltinExchangeType.DIRECT;
					}
					
				});
			}
		};
		
		final IAMQPInitializer initializer = new AMQPInitializer();
		initializer.initialize(null, provider);
	}
}
