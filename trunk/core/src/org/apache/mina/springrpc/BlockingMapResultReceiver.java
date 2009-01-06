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

package org.apache.mina.springrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xl.util.concurrent.BlockingMap;

/**
 * 
 * @author politics wang
 * @since 2008-12-08
 *
 */
public class BlockingMapResultReceiver implements ResultReceiver {

	private static Logger logger = LoggerFactory.getLogger(BlockingMapResultReceiver.class);
	
	private BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult> results 
		= new BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult>();
	
	@Override
	public ReturnAddressAwareRemoteInvocationResult takeResult(ReturnAddress returnAddress) {
		try {
			return (ReturnAddressAwareRemoteInvocationResult) results.take(returnAddress);
		} catch (InterruptedException e) {
			String message = "Client disconnected !!!";
			if (logger.isErrorEnabled()) {
				logger.error(message, e);
			}
			return new ReturnAddressAwareRemoteInvocationResult(returnAddress, e);
		}	
	}

	@Override
	public void resultReceived(ReturnAddressAwareRemoteInvocationResult result) {
		results.put(result.getReturnAddress(), result);
	}

	@Override
	public void interrupt() {
		results.clear();
	}

	
}
