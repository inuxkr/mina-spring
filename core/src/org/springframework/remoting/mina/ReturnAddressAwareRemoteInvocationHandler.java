package org.springframework.remoting.mina;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public interface ReturnAddressAwareRemoteInvocationHandler {

	ReturnAddressAwareRemoteInvocationResult invoke(ReturnAddressAwareRemoteInvocation invocation);

}
