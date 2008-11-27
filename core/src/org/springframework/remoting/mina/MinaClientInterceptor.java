/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.remoting.mina;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationBasedAccessor;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class MinaClientInterceptor extends RemoteInvocationBasedAccessor 
	implements MethodInterceptor, DisposableBean, MinaClientConfiguration {

	private MinaRequestExecutor minaRequestExecutor;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		ReturnAddressAwareRemoteInvocation invocation = createReturnAddressAwareRemoteInvocation(methodInvocation);
		ReturnAddressAwareRemoteInvocationResult result = null;
		
		try {
			result = executeRequest(invocation, methodInvocation);
		}
		catch (Throwable ex) {
			throw convertMinaAccessException(ex);
		}
		try {
			return recreateRemoteInvocationResult(result);
		}
		catch (Throwable ex) {
			if (result.hasInvocationTargetException()) {
				throw ex;
			}
			else {
				throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod() +
						"] failed in mina remote service at [" + getServiceUrl() + "]", ex);
			}
		}
	}

	private ReturnAddressAwareRemoteInvocationResult executeRequest(
		ReturnAddressAwareRemoteInvocation invocation, 
		MethodInvocation methodInvocation) throws Exception {
		
		return getMinaRequestExecutor().executeRequest(invocation);
	}

	private MinaRequestExecutor getMinaRequestExecutor() {
		return minaRequestExecutor;
	}

	private ReturnAddressAwareRemoteInvocation createReturnAddressAwareRemoteInvocation(MethodInvocation methodInvocation) {
		RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
		ReturnAddress returnAddress = new UniqueStringReturnAddress();
		return new ReturnAddressAwareRemoteInvocation(returnAddress, invocation);
	}

	protected RemoteAccessException convertMinaAccessException(Throwable ex) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not yet implement");
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		try {
			initializeMinaRequestExecutor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	private void initializeMinaRequestExecutor() throws Exception {
		minaRequestExecutor = new DefaultMinaRequestExecutor();
		minaRequestExecutor.setMinaClientConfiguration(this);
		minaRequestExecutor.afterPropertiesSet();
	}

	@Override
	public void destroy() throws Exception {
		minaRequestExecutor.destroy();
	}

	@Override
	public String getHostName() {
		try {
			return getURL().getHost();
		} catch (MalformedURLException e) {
			throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", e);
		}
	}

	private URL getURL() throws MalformedURLException {
		try {
			URL url = new URL(null, getServiceUrl(), new DummyURLStreamHandler());
			String protocol = url.getProtocol();
			if (protocol != null && !"tcp".equals(protocol)) {
				throw new MalformedURLException("Invalid URL scheme '" + protocol + "'");
			}
			return url;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getPort() {
		try {
			return getURL().getPort();
		} catch (MalformedURLException e) {
			throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", e);
		}
	}

	/**
	 * Dummy URLStreamHandler that's just specified to suppress the standard
	 * <code>java.net.URL</code> URLStreamHandler lookup, to be able to
	 * use the standard URL class for parsing "rmi:..." URLs.
	 */
	private static class DummyURLStreamHandler extends URLStreamHandler {

		protected URLConnection openConnection(URL url) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

	
}
