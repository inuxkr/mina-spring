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

import org.apache.mina.core.session.IoSession;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 *
 * @author politics wang
 * @since Dec 14, 2008
 *
 */
public class MinaClientHandlerTest {

	@Test
	public final void messageReceived() throws Exception {
		ResultReceiver resultReceiver = EasyMock.createMock(ResultReceiver.class);
		MinaRequestExecutor requestExecutor = EasyMock.createMock(MinaRequestExecutor.class);
		IoSession session = null;
		ReturnAddressAwareRemoteInvocationResult message = new ReturnAddressAwareRemoteInvocationResult(null, null);
		resultReceiver.resultReceived(message);
		EasyMock.expectLastCall().asStub();
		
		Object[] mocks = new Object[] {resultReceiver, requestExecutor};
		EasyMock.replay(mocks);
		
		MinaClientHandler clientHandler = new MinaClientHandler(resultReceiver, requestExecutor);
		clientHandler.messageReceived(session, message);
	
		EasyMock.verify(mocks);
	}

	@Test
	public final void sessionClosedWhenExecutorRunning() throws Exception {
		sessionClosed(true);
	}
	
	@Test
	public final void sessionClosedWhenExecutorDestroyed() throws Exception {
		sessionClosed(false);
	}

	private void sessionClosed(boolean isRunning) throws Exception {
		ResultReceiver resultReceiver = EasyMock.createMock(ResultReceiver.class);
		MinaRequestExecutor requestExecutor = EasyMock.createMock(MinaRequestExecutor.class);
		EasyMock.expect(requestExecutor.isRunning()).andReturn(isRunning);
		IoSession session = null;
		
		if (isRunning) {
			requestExecutor.connect();
			EasyMock.expectLastCall().asStub();
		}
		
		Object[] mocks = new Object[] {resultReceiver, requestExecutor};
		EasyMock.replay(mocks);
		
		MinaClientHandler clientHandler = new MinaClientHandler(resultReceiver, requestExecutor);
		clientHandler.sessionClosed(session);
	
		EasyMock.verify(mocks);
	}

}
