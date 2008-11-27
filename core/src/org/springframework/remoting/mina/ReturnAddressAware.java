package org.springframework.remoting.mina;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public interface ReturnAddressAware {
	
	ReturnAddress getReturnAddress();
	
	void setReturnAddress(ReturnAddress returnAddress);
	
}
