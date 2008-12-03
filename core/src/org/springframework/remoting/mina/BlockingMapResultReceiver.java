package org.springframework.remoting.mina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xl.util.concurrent.BlockingMap;

public class BlockingMapResultReceiver implements ResultReceiver {

	private static Logger logger = LoggerFactory.getLogger(BlockingMapResultReceiver.class);
	
	private BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult> results 
	= new BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult>();
	
	@Override
	public ReturnAddressAwareRemoteInvocationResult getResult(ReturnAddress returnAddress) {
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

	@Override
	public void resultReceived(ReturnAddressAwareRemoteInvocationResult result) {
		results.put(result.getReturnAddress(), result);
	}

	
}
