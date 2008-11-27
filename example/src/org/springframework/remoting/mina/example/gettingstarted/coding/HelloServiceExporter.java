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

import org.springframework.remoting.mina.MinaServiceExporter;
import org.springframework.remoting.mina.example.gettingstarted.DefaultHelloService;
import org.springframework.remoting.mina.example.gettingstarted.HelloService;


public class HelloServiceExporter {

	public static void main(String[] args) throws Exception {
		MinaServiceExporter exporter = new MinaServiceExporter();
		HelloService service = new DefaultHelloService();
		exporter.setService(service);
		exporter.setServiceInterface(HelloService.class);
		exporter.afterPropertiesSet();
	}

}
