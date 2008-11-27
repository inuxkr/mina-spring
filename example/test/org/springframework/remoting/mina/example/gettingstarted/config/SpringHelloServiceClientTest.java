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

package org.springframework.remoting.mina.example.gettingstarted.config;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.mina.example.gettingstarted.AbstractHelloServiceClientTests;
import org.springframework.remoting.mina.example.gettingstarted.HelloService;


/**
 *
 * @autoor politics wang`
 * @since 2008-11-27
 *
 */
public class SpringHelloServiceClientTest extends AbstractHelloServiceClientTests {

	@Test
	public void test() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("hello-service-client.xml", getClass());
		String beanName = "helloService";
		HelloService service = (HelloService) context.getBean(beanName);
		invokeService(service);
		((DisposableBean) context.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName)).destroy();
	}
	
}
