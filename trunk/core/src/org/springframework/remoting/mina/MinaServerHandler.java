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

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServerHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

	private ReturnAddressAwareRemoteInvocationHandler invocationHandler;
	
	public MinaServerHandler(ReturnAddressAwareRemoteInvocationHandler remoteInvocationInvoker) {
		Assert.notNull(remoteInvocationInvoker, "remoteInvocationInvoker required");
		this.invocationHandler = remoteInvocationInvoker;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Message received : " + message);
		}
		Assert.isInstanceOf(ReturnAddressAwareRemoteInvocation.class, message);
		ReturnAddressAwareRemoteInvocation invocation = (ReturnAddressAwareRemoteInvocation) message;
		ReturnAddressAwareRemoteInvocationResult result = invocationHandler.invoke(invocation);
		session.write(result);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		//TODO handle exception
		logger.error(cause.getMessage(), cause);
		throw new UnsupportedOperationException("NYI");
	}


}
