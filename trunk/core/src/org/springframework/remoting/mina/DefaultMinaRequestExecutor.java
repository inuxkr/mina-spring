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

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 */
public class DefaultMinaRequestExecutor implements MinaRequestExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultMinaRequestExecutor.class);
	
	private IoConnector connector = new NioSocketConnector();
	
	private IoSession session;
	
	private MinaClientConfiguration configuration;

	private MinaServiceClientHandler handler = new MinaServiceClientHandler();

	
	@Override
	public ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) 
		throws Exception {
		
		WriteFuture writeFuture = session.write(invocation);
		writeFuture.await();
		return handler.getReceivedMessage(invocation.getReturnAddress());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialize();
	}

	public void initialize() {
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(handler);
		ConnectFuture future = connector.connect(getAddress());
		future.awaitUninterruptibly();
		session = future.getSession();		
	}

	private InetSocketAddress getAddress() {
		return new InetSocketAddress(configuration.getHostName(), configuration.getPort());
	}

	@Override
	public void destroy() throws Exception {
		try {
			session.closeOnFlush().await();
		} catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
				logger.error("Close session failed : " + e.getMessage(), e);
			}
		}
		connector.dispose();
	}

	@Override
	public void setMinaClientConfiguration(MinaClientConfiguration configuration) {
		this.configuration = configuration;
	}

}
