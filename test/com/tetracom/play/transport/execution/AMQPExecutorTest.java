package com.tetracom.play.transport.execution;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import com.tetracom.play.transport.actions.IAMQPPublishAction;
import com.tetracom.play.transport.actions.INoChannelHandler;
import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

public class AMQPExecutorTest {
	
	@Test(expected = NullPointerException.class)
	public void executePublishActionWithMessages() {
		final AMQPExecutor executor = new AMQPExecutor(null, null);
		executor.execute( new IAMQPPublishAction() {
			
			@Override
			public Optional<IAMQPQueue> queue() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPExchange exchange() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Optional<IAMQPQueue> replyTo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction replyTo(IAMQPQueue queue) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Optional<INoChannelHandler> noChannelHandler() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction noChannelHandler(INoChannelHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<byte[]> messages() {
				final List<byte[]> result = new ArrayList<>();
				result.add(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
				return result;
			}
			
			@Override
			public IAMQPPublishAction messages(Collection<byte[]> messages) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction message(byte[] message) {
				// TODO Auto-generated method stub
				return null;
			}
		} );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executePublishActionWithoutMessages() {
		final AMQPExecutor executor = new AMQPExecutor(null, null);
		executor.execute( new IAMQPPublishAction() {
			
			@Override
			public Optional<IAMQPQueue> queue() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPExchange exchange() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Optional<IAMQPQueue> replyTo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction replyTo(IAMQPQueue queue) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Optional<INoChannelHandler> noChannelHandler() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction noChannelHandler(INoChannelHandler handler) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<byte[]> messages() {
				return Collections.emptyList();
			}
			
			@Override
			public IAMQPPublishAction messages(Collection<byte[]> messages) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public IAMQPPublishAction message(byte[] message) {
				// TODO Auto-generated method stub
				return null;
			}
		} );
	}
	
}
