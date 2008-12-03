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
import org.springframework.util.Assert;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 */
public class DefaultMinaRequestExecutor implements MinaRequestExecutor {
	
	private IoConnector connector;
	
	private IoSession session;
	
	private MinaClientConfiguration configuration;

	private ResultReceiver resultReceiver;

	
	@Override
	public ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) 
		throws Exception {
		
		WriteFuture writeFuture = session.write(invocation);
		writeFuture.awaitUninterruptibly();
		return resultReceiver.getResult(invocation.getReturnAddress());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(connector, "connector required");
		Assert.notNull(resultReceiver, "resultReceiver required");
		initialize();
	}

	public void initialize() {
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(new MinaClientHandler(resultReceiver));
		ConnectFuture future = connector.connect(getAddress());
		future.awaitUninterruptibly();
		session = future.getSession();		
	}

	private InetSocketAddress getAddress() {
		return new InetSocketAddress(configuration.getHostName(), configuration.getPort());
	}

	@Override
	public void destroy() throws Exception {
		session.closeOnFlush().awaitUninterruptibly();
		connector.dispose();
	}

	@Override
	public void setMinaClientConfiguration(MinaClientConfiguration configuration) {
		this.configuration = configuration;
	}

	public void setResultReceiver(ResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public void setConnector(IoConnector connector) {
		this.connector = connector;
	}
	
	

}
