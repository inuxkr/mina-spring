package org.springframework.remoting.mina;

import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class ReturnAddressAwareRemoteInvocationResult extends
		RemoteInvocationResult implements ReturnAddressAware {

	private static final long serialVersionUID = -9151370144617233397L;
	
	private ReturnAddress returnAddress;
	
	public ReturnAddressAwareRemoteInvocationResult(ReturnAddress returnAddress, Object value) {
		super(value);
		setReturnAddress(returnAddress);
	}
	
	public ReturnAddressAwareRemoteInvocationResult(ReturnAddress returnAddress, Exception exception) {
		super(exception);
		setReturnAddress(returnAddress);
	}
	
	
	@Override
	public ReturnAddress getReturnAddress() {
		return returnAddress;
	}

	@Override
	public void setReturnAddress(ReturnAddress returnAddress) {
		this.returnAddress = returnAddress;
	}

}
