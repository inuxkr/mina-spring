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
import org.xl.util.concurrent.BlockingMap;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServiceClientHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult> results 
		= new BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult>();
	
	@Override
	public void messageReceived(IoSession session, Object message)
		throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.trace("Message received : " + message);
		}
		ReturnAddressAwareRemoteInvocationResult result = (ReturnAddressAwareRemoteInvocationResult) message;
		results.put(result.getReturnAddress(), result);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.trace("Message sent : " + message);
		}		
	}
	
	public ReturnAddressAwareRemoteInvocationResult getReceivedMessage(ReturnAddress returnAddress) {
		try {
			ReturnAddressAwareRemoteInvocationResult invocation = (ReturnAddressAwareRemoteInvocationResult) results.take(returnAddress);
			return invocation;
		} catch (InterruptedException e) {
			String message = "Receive Message failed : " + e.getMessage();
			if (logger.isErrorEnabled()) {
				logger.error(message, e);
			}
			throw new RuntimeException(message, e);
		}
	}

}
