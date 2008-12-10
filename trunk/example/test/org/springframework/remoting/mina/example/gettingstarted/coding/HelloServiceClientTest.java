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

package org.springframework.remoting.mina.example.gettingstarted.coding;


import org.junit.Test;
import org.springframework.remoting.mina.MinaProxyFactoryBean;
import org.springframework.remoting.mina.NioSocketAcceptorFactoryBean;
import org.springframework.remoting.mina.example.gettingstarted.AbstractHelloServiceClientTests;
import org.springframework.remoting.mina.example.gettingstarted.HelloService;


/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class HelloServiceClientTest extends AbstractHelloServiceClientTests {
	
	@Test
	public void test() throws Exception {
		MinaProxyFactoryBean bean = new MinaProxyFactoryBean();
		bean.setServiceUrl("tcp://localhost:" + NioSocketAcceptorFactoryBean.DEFAULT_PORT);
		bean.setServiceInterface(HelloService.class);
		bean.afterPropertiesSet();
		HelloService service = (HelloService) bean.getObject();
		invokeService(service);
		bean.destroy();
	}
	
}
