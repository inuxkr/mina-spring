package org.springframework.remoting.mina;

import org.springframework.remoting.support.RemoteInvocation;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class ReturnAddressAwareRemoteInvocation extends RemoteInvocation implements ReturnAddressAware {
	
	private static final long serialVersionUID = 5659901278458148937L;
	
	private ReturnAddress returnAddress;

	public ReturnAddressAwareRemoteInvocation(ReturnAddress returnAddress, RemoteInvocation invocation) {
		setReturnAddress(returnAddress);
		setArguments(invocation.getArguments());
		setAttributes(invocation.getAttributes());
		setMethodName(invocation.getMethodName());
		setParameterTypes(invocation.getParameterTypes());
	}

	public ReturnAddress getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(ReturnAddress returnAddress) {
		this.returnAddress = returnAddress;
	}
	
	
	
}
