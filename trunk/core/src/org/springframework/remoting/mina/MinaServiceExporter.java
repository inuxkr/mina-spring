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

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServiceExporter extends RemoteInvocationBasedExporter implements InitializingBean, DisposableBean {

	public static final int DEFAULT_PORT = 22222;
	public static final int DEFAULT_READ_BUFFER_SIZE = 2048;
	public static final int DEFAULT_IDLE_TIME = 10;

	private IoAcceptor acceptor = new NioSocketAcceptor();
	private int port = DEFAULT_PORT;
	private int readBufferSize = DEFAULT_READ_BUFFER_SIZE;
	private int idleTime = DEFAULT_IDLE_TIME;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		prepare();
	}

	private void prepare() throws Exception {
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		ReturnAddressAwareRemoteInvocationHandler invocationHandler = new MinaRemoteInvocationHandler();
		acceptor.setHandler(new MinaServiceServerHandler(invocationHandler));
		acceptor.getSessionConfig().setReadBufferSize(readBufferSize);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
		acceptor.bind(new InetSocketAddress(port));
	}
	
	private class MinaRemoteInvocationHandler implements ReturnAddressAwareRemoteInvocationHandler {
		@Override
		public ReturnAddressAwareRemoteInvocationResult invoke(ReturnAddressAwareRemoteInvocation invocation) {
			RemoteInvocationResult result = invokeAndCreateResult(invocation, getService());
			if (result.hasException()) {
				return new ReturnAddressAwareRemoteInvocationResult(invocation.getReturnAddress(), result.getException());
			}
			return new ReturnAddressAwareRemoteInvocationResult(invocation.getReturnAddress(), result.getValue()); 
		}
	}

	@Override
	public void destroy() throws Exception {
		acceptor.unbind();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

}
