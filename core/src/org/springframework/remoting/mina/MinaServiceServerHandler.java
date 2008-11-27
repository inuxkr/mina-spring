package org.springframework.remoting.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.util.Assert;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaServiceServerHandler extends IoHandlerAdapter {

	private ReturnAddressAwareRemoteInvocationHandler invocationHandler;
	
	public MinaServiceServerHandler(ReturnAddressAwareRemoteInvocationHandler remoteInvocationInvoker) {
		this.invocationHandler = remoteInvocationInvoker;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Assert.isInstanceOf(ReturnAddressAwareRemoteInvocation.class, message);
		ReturnAddressAwareRemoteInvocation invocation = (ReturnAddressAwareRemoteInvocation) message;
		ReturnAddressAwareRemoteInvocationResult result = invocationHandler.invoke(invocation);
		session.write(result);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not yet implement");
	}


}
