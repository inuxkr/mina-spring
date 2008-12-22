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

package org.springframework.remoting.mina.example.gettingstarted;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultHelloService implements HelloService {

	private static Logger logger = LoggerFactory.getLogger(DefaultHelloService.class);  
	
	private static AtomicInteger counter = new AtomicInteger();
	
	@Override
	public HelloResponse sayHello(HelloRequest helloRequest) {
		HelloResponse helloResponse = new HelloResponse();
		helloResponse.setSequence(counter.incrementAndGet());
		if (logger.isDebugEnabled()) {
			logger.debug(helloResponse.toString());
		}
		return helloResponse;
	}

}
