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
public class MinaServiceServerHandler extends IoHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
		//TODO handle exception
		logger.error(cause.getMessage(), cause);
	}


}
