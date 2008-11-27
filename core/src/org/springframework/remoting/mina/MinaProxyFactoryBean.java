package org.springframework.remoting.mina;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaProxyFactoryBean extends MinaClientInterceptor implements FactoryBean {

	private Object serviceProxy;

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (getServiceInterface() == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}


	public Object getObject() {
		return this.serviceProxy;
	}

	public Class getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}


}
