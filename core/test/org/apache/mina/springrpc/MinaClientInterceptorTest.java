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

package org.apache.mina.springrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.remoting.RemoteAccessException;

/**
 * 
 * @author politics wang
 * @since 2008-12-09
 *
 */
public class MinaClientInterceptorTest {

	@Test
	public final void createDefaultConnector() {
		MinaRequestExecutor minaRequestExecutor = EasyMock.createMock(MinaRequestExecutor.class);
		
		EasyMock.replay(minaRequestExecutor);
		
		MinaClientInterceptor interceptor = new MinaClientInterceptor();
		interceptor.setServiceUrl("tcp://localhost:8012");
		interceptor.setMinaRequestExecutor(minaRequestExecutor);
		interceptor.afterPropertiesSet();
		assertNotNull(interceptor.getConnector());
	
		EasyMock.verify(minaRequestExecutor);
	}
	
	@Test
	public final void invokeCorrectly() throws Throwable {
		Object expected = new Object();
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		Method method = Object.class.getMethod("hashCode", new Class[] { });
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method).anyTimes();
		Object[] arguments = new Object[] { };
		EasyMock.expect(methodInvocation.getArguments()).andReturn(arguments).anyTimes();
		
		MinaRequestExecutor minaRequestExecutor = EasyMock.createMock(MinaRequestExecutor.class);
		ReturnAddress returnAddress = new UniqueStringReturnAddress();
		ReturnAddressAwareRemoteInvocationResult result = new ReturnAddressAwareRemoteInvocationResult(returnAddress, expected);
		EasyMock.expect(minaRequestExecutor.executeRequest((ReturnAddressAwareRemoteInvocation) EasyMock.anyObject())).andReturn(result);
		
		Object[] mocks = new Object[] {methodInvocation, minaRequestExecutor};
		
		EasyMock.replay(mocks);
		
		MinaClientInterceptor interceptor = new MinaClientInterceptor();
		interceptor.setServiceUrl("tcp://localhost:8012");
		interceptor.setMinaRequestExecutor(minaRequestExecutor);
		interceptor.afterPropertiesSet();
		Object actual = interceptor.invoke(methodInvocation);
		assertEquals(expected, actual);
		
		EasyMock.verify(mocks);
	}
	
	@Test(expected = RemoteAccessException.class)
	public void exceptionCaught() throws Throwable {
		MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
		
		Method method = Object.class.getMethod("hashCode", new Class[] { });
		EasyMock.expect(methodInvocation.getMethod()).andReturn(method).anyTimes();
		Object[] arguments = new Object[] { };
		EasyMock.expect(methodInvocation.getArguments()).andReturn(arguments).anyTimes();
		
		MinaRequestExecutor minaRequestExecutor = EasyMock.createMock(MinaRequestExecutor.class);
		Throwable throwable = new Exception("Test Exception");
		EasyMock.expect(minaRequestExecutor.executeRequest((ReturnAddressAwareRemoteInvocation) EasyMock.anyObject())).andThrow(throwable );
		
		Object[] mocks = new Object[] {methodInvocation, minaRequestExecutor};
		
		EasyMock.replay(mocks);
		
		MinaClientInterceptor interceptor = new MinaClientInterceptor();
		interceptor.setServiceUrl("tcp://localhost:8012");
		interceptor.setMinaRequestExecutor(minaRequestExecutor);
		interceptor.afterPropertiesSet();
		interceptor.invoke(methodInvocation);
	}

}
