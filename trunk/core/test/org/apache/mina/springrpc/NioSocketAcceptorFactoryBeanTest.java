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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.junit.Test;


/**
 *
 * @author politics wang
 * @since Dec 10, 2008
 *
 */
public class NioSocketAcceptorFactoryBeanTest {
	
	@Test
	public void getObjectType() {
		assertSame(NioSocketAcceptor.class, new NioSocketAcceptorFactoryBean().getObjectType());
	}
	
	@Test
	public void isSingleton() {
		assertTrue(new NioSocketAcceptorFactoryBean().isSingleton());
	}
	
	@Test
	public void settings() throws Exception {
		int expectedPort = 8012;
		int expectedReadBufferSize = 1024;
		int expectedBothIdleTime = 20;
		NioSocketAcceptorFactoryBean factoryBean = new NioSocketAcceptorFactoryBean();
		factoryBean.setPort(expectedPort);
		factoryBean.setReadBufferSize(expectedReadBufferSize);
		factoryBean.setIdleTime(expectedBothIdleTime);
		NioSocketAcceptor acceptor = (NioSocketAcceptor) factoryBean.getObject();;
		assertEquals(expectedPort, acceptor.getDefaultLocalAddress().getPort());
		assertEquals(expectedReadBufferSize, acceptor.getSessionConfig().getReadBufferSize());
		assertEquals(expectedBothIdleTime, acceptor.getSessionConfig().getBothIdleTime());
	}
	
}
