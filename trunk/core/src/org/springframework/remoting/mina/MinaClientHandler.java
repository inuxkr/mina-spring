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
public class MinaClientHandler extends IoHandlerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ResultReceiver resultReceiver;

	private final MinaRequestExecutor requestExecutor;
	
	public MinaClientHandler(ResultReceiver resultReceiver, MinaRequestExecutor requestExecutor) {
		Assert.notNull(resultReceiver, "resultReceiver required");
		Assert.notNull(requestExecutor, "requestExecutor required");
		this.resultReceiver = resultReceiver;
		this.requestExecutor = requestExecutor;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
		throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Message received : " + message);
		}
		ReturnAddressAwareRemoteInvocationResult result = (ReturnAddressAwareRemoteInvocationResult) message;
		resultReceiver.resultReceived(result);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Message sent : " + message);
		}		
	}
	
    @Override
	public void sessionClosed(IoSession session) throws Exception {
    	boolean running = requestExecutor.isRunning();
    	if (!running) {
    		if (logger.isInfoEnabled()) {
    			logger.info("ReqeustExecutor is not running, do nothing");
    		}
    		return;
    	}
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("Session closed, try reconnect to server");
    	}
    	requestExecutor.connect();
	}


}
