package org.springframework.remoting.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.xl.util.concurrent.BlockingMap;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServiceClientHandler extends IoHandlerAdapter {

	private BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult> results 
		= new BlockingMap<ReturnAddress, ReturnAddressAwareRemoteInvocationResult>();
	
	@Override
	public void messageReceived(IoSession session, Object message)
		throws Exception {
		
		System.out.println("Received message " + message);
		ReturnAddressAwareRemoteInvocationResult result = (ReturnAddressAwareRemoteInvocationResult) message;
		results.put(result.getReturnAddress(), result);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("Sent message " + message);
	}
	
	public ReturnAddressAwareRemoteInvocationResult getReceivedMessage(ReturnAddress returnAddress) {
		try {
			ReturnAddressAwareRemoteInvocationResult invocation = (ReturnAddressAwareRemoteInvocationResult) results.take(returnAddress);
			return invocation;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
