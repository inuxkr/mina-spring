/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.remoting.mina;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.remoting.support.RemoteInvocation;

/**
 *
 * @author politics wang
 * @since 2008-11-28
 *
 */
public class DefaultMinaRequestExecutorTest {

	
	@Test
	public void connect() {
		IoConnector connector = EasyMock.createMock(IoConnector.class);
		
		MinaClientConfiguration configuration = new StaticConfiguration();
		InetSocketAddress socketAddress = new InetSocketAddress(configuration.getHostName(), configuration.getPort());
		
		ResultReceiver resultReceiver = EasyMock.createMock(ResultReceiver.class);
		resultReceiver.interrupt();
		EasyMock.expectLastCall().asStub();
		EasyMock.expect(connector.connect(socketAddress)).andThrow(new RuntimeException("server crashed"));
		ConnectFuture connectFuture = EasyMock.createMock(ConnectFuture.class);
		EasyMock.expect(connector.connect(socketAddress)).andReturn(connectFuture);
		EasyMock.expect(connectFuture.awaitUninterruptibly()).andReturn(connectFuture);
		IoSession session = EasyMock.createMock(IoSession.class);
		EasyMock.expect(connectFuture.getSession()).andReturn(session);
		
		Object[] mocks = new Object[] {resultReceiver, connector, connectFuture, session};
		EasyMock.replay(mocks);
		
		DefaultMinaRequestExecutor executor = new DefaultMinaRequestExecutor();
		executor.setMinaClientConfiguration(configuration);
		executor.setConnector(connector);
		executor.setResultReceiver(resultReceiver);
		executor.connect();
	
		EasyMock.verify(mocks);
	}
	
	@Test
	public void executeRequest() throws Exception {
		ReturnAddress returnAddress = new UniqueStringReturnAddress();
		Object value = new Object();
		ReturnAddressAwareRemoteInvocationResult expected = new ReturnAddressAwareRemoteInvocationResult(returnAddress, value);

		MinaClientConfiguration configuration = new StaticConfiguration();
		IoConnector connector = EasyMock.createMock(IoConnector.class);		
		connector.setHandler((IoHandler) EasyMock.anyObject());
		EasyMock.expectLastCall().asStub();
		
		ResultReceiver resultReceiver = EasyMock.createMock(ResultReceiver.class);
		resultReceiver.interrupt();
		EasyMock.expectLastCall().asStub();
				
		ConnectFuture connectFuture = EasyMock.createMock(ConnectFuture.class);
		InetSocketAddress socketAddress = new InetSocketAddress(configuration.getHostName(), configuration.getPort());
		EasyMock.expect(connector.connect(socketAddress)).andReturn(connectFuture);
		EasyMock.expect(connectFuture.awaitUninterruptibly()).andReturn(connectFuture);
		IoSession session = EasyMock.createMock(IoSession.class);
		EasyMock.expect(connectFuture.getSession()).andReturn(session );
		
		RemoteInvocation decorated = RemoteInvocationFactory.createHashCodeRemoteInvocation();
		ReturnAddressAwareRemoteInvocation invocation = new ReturnAddressAwareRemoteInvocation(returnAddress, decorated);
		WriteFuture writeFuture = EasyMock.createMock(WriteFuture.class);
		EasyMock.expect(session.write(invocation)).andReturn(writeFuture);
//		EasyMock.expect(writeFuture.awaitUninterruptibly()).andReturn(writeFuture);
		
		resultReceiver.resultReceived(expected);
		EasyMock.expectLastCall().asStub();
		EasyMock.expect(resultReceiver.takeResult(returnAddress)).andReturn(expected);
		
		CloseFuture closeFuture = EasyMock.createMock(CloseFuture.class);
		EasyMock.expect(session.close(true)).andReturn(closeFuture);
//		EasyMock.expect(closeFuture.awaitUninterruptibly()).andReturn(closeFuture);
		connector.dispose();
		EasyMock.expectLastCall().asStub();
		
		Object[] mocks = new Object[] {connector, connectFuture, session, writeFuture, resultReceiver, closeFuture};

		EasyMock.replay(mocks);

		MinaRequestExecutor executor = new DefaultMinaRequestExecutor();
		executor.setMinaClientConfiguration(configuration);
		executor.setConnector(connector);
		executor.setResultReceiver(resultReceiver);
		executor.afterPropertiesSet();
		ReturnAddressAwareRemoteInvocationResult result = executor.executeRequest(invocation);
		assertEquals(expected , result);
		executor.destroy();
		
		EasyMock.verify(mocks);
	}
	
	private static final class StaticConfiguration implements MinaClientConfiguration {
		@Override
		public String getHostName() {
			return "localhost";
		}
		@Override
		public int getPort() {
			return NioSocketAcceptorFactoryBean.DEFAULT_PORT;
		}
	}
	
}
