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

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.remoting.support.RemoteInvocation;

/**
 *
 * @author politics wang
 * @since Dec 10, 2008
 *
 */
public class MinaServerHandlerTest {

	@Test(expected = IllegalArgumentException.class)
	public final void messageReceivedInvalidType() throws Exception {
		ReturnAddressAwareRemoteInvocationHandler remoteInvocationInvoker = EasyMock.createMock(ReturnAddressAwareRemoteInvocationHandler.class);
		MinaServerHandler handler = new MinaServerHandler(remoteInvocationInvoker );
		Object invalidType = new  Object();
		handler.messageReceived(null, invalidType );
	}

	
	@Test
	public final void messageReceivedCorrectly() throws Exception {
		ReturnAddressAwareRemoteInvocationHandler remoteInvocationInvoker = EasyMock.createMock(ReturnAddressAwareRemoteInvocationHandler.class);
		IoSession session = EasyMock.createMock(IoSession.class);
		RemoteInvocation decorated = RemoteInvocationFactory.createHashCodeRemoteInvocation();
		ReturnAddressAwareRemoteInvocation invocation = new ReturnAddressAwareRemoteInvocation(new UniqueStringReturnAddress(), decorated);
		
		ReturnAddressAwareRemoteInvocationResult result = new ReturnAddressAwareRemoteInvocationResult(invocation.getReturnAddress(), new Object());
		EasyMock.expect(remoteInvocationInvoker.invoke(invocation)).andReturn(result);
		WriteFuture writeFuture = null;
		EasyMock.expect(session.write(result)).andReturn(writeFuture);
		
		Object[] mocks = new Object[] {remoteInvocationInvoker, session};
		EasyMock.replay(mocks);
		
		MinaServerHandler handler = new MinaServerHandler(remoteInvocationInvoker);
		handler.messageReceived(session, invocation);
	
		EasyMock.verify(mocks);
	}
	
	@Test
	public final void exceptionCaught() {
//		fail("Not yet implemented"); // TODO
	}

}
