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

import org.apache.mina.core.service.IoAcceptor;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Test;


/**
 *
 * @author politics wang
 * @since Dec 10, 2008
 *
 */
public class MinaServiceExporterTest {
	
	@Test
	public void test() throws Exception {
		IoAcceptor ioAcceptor = EasyMock.createMock(IoAcceptor.class);
		EasyMock.reportMatcher(new IArgumentMatcher() {
			@Override
			public void appendTo(StringBuffer buffer) {
			}
			@Override
			public boolean matches(Object argument) {
				return MinaServerHandler.class.isInstance(argument);
			}
		});
		ioAcceptor.setHandler(null);
		EasyMock.expectLastCall().asStub();
		ioAcceptor.bind();
		EasyMock.expectLastCall().asStub();
		ioAcceptor.unbind();
		EasyMock.expectLastCall().asStub();
		
		Object[] mocks = new Object[] {ioAcceptor};
		EasyMock.replay(mocks);
		
		MinaServiceExporter exporter = new MinaServiceExporter();
		exporter.setIoAcceptor(ioAcceptor);
		exporter.afterPropertiesSet();
		exporter.destroy();
		
		EasyMock.verify(mocks);
	}
	
	
}
