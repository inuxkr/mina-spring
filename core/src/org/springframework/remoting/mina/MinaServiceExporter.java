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

import org.apache.mina.core.service.IoAcceptor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.Assert;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServiceExporter extends RemoteInvocationBasedExporter implements InitializingBean, DisposableBean {

	private IoAcceptor acceptor;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		prepare();
	}

	private void prepare() throws Exception {
		Assert.notNull(acceptor, "acceptor requried");
		ReturnAddressAwareRemoteInvocationHandler invocationHandler = new MinaRemoteInvocationHandler();
		acceptor.setHandler(new MinaServerHandler(invocationHandler));
		acceptor.bind();
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

	public void setIoAcceptor(IoAcceptor ioAcceptor) {
		this.acceptor = ioAcceptor;
	}

}
