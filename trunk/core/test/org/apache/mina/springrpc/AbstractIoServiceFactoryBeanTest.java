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

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.junit.Test;

/**
 *
 * @author politics wang
 * @since Dec 22, 2008
 *
 */
public class AbstractIoServiceFactoryBeanTest {

	private static final int EXTRA_FILTER_SIZE = 10;

	@Test
	public void protocolCodecFilterBuiltIn() throws Exception {
		AbstractIoServiceFactoryBean factoryBean = new IoServiceFactoryBeanStub();
		IoService service = new NioSocketAcceptor();
		factoryBean.configFilters(service);
		assertTrue(service.getFilterChain().contains(ProtocolCodecFilter.class));
	}
	
	@Test
	public void extraFilters() throws Exception {
		AbstractIoServiceFactoryBean factoryBean = new IoServiceFactoryBeanStub();
		Map<String, IoFilter> extraFilters = new HashMap<String, IoFilter>();
		
		for (int i = 0; i < EXTRA_FILTER_SIZE; i++) {
			extraFilters.put("extraFilter" + i, new IoFilterAdapter());
		}
				
		factoryBean.setExtraFilters(extraFilters);
		
		IoService service = new NioSocketAcceptor();
		factoryBean.configFilters(service);
		
		for (IoFilter filter : extraFilters.values()) {
			assertTrue(service.getFilterChain().contains(filter));
		}
		
		assertTrue(service.getFilterChain().contains(ProtocolCodecFilter.class));		
	}	
	
	private static final class IoServiceFactoryBeanStub extends AbstractIoServiceFactoryBean {

		@Override
		public Object getObject() throws Exception {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<?> getObjectType() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isSingleton() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	
}
