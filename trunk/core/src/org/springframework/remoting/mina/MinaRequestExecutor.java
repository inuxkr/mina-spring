package org.springframework.remoting.mina;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public interface MinaRequestExecutor extends InitializingBean, DisposableBean {
	
	void setMinaClientConfiguration(MinaClientConfiguration configuration);
	
	ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) throws Exception;

}
