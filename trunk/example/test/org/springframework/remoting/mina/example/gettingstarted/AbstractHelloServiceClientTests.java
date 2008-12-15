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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @autoor politics wang`
 * @since 2008-11-27
 *
 */
public abstract class AbstractHelloServiceClientTests {

	public Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final int CONCURRENT_SIZE = 10;
	private static final int RUN_TIMES = 10000;

	protected void invokeService(final HelloService service) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_SIZE);
		
		List<Future<HelloResponse>> futures = new ArrayList<Future<HelloResponse>>();
		
		for (int i = 0; i < RUN_TIMES; i++) {
			Future<HelloResponse> future = executor.submit(new Callable<HelloResponse>() {
				@Override
				public HelloResponse call() throws Exception {
					HelloResponse response = service.sayHello(new HelloRequest());
					logger.info(response.toString());
					return response;
				}
			});
			
			futures.add(future);
		}
		
		for (Future<HelloResponse> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
	}

}
