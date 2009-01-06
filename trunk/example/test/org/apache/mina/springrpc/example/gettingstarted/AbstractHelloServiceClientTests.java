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

package org.apache.mina.springrpc.example.gettingstarted;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

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

	private boolean useConcurrent = true;
	
	private AtomicInteger counter = new AtomicInteger();
	
	
	protected void invokeService(final HelloService service) {
		StopWatch sw = new StopWatch("HelloService with concurrent [ " + useConcurrent + " ], runtimes [ " + RUN_TIMES + " ]");
		sw.start();
		
		List<Callable<HelloResponse>> tasks = createTasks(service);
		
		if (useConcurrent) {
			executeUseConcurrent(tasks);
		} else {
			execute(tasks);
		}
		
		sw.stop();
		logger.info(sw.prettyPrint());
	}

	private List<Callable<HelloResponse>> createTasks(final HelloService service) {
		List<Callable<HelloResponse>> tasks = new ArrayList<Callable<HelloResponse>>(RUN_TIMES);
		
		
		for (int i = 0; i < RUN_TIMES; i++) {
			Callable<HelloResponse> task = new Callable<HelloResponse>() {
				@Override
				public HelloResponse call() throws Exception {
					HelloResponse response = service.sayHello(new HelloRequest());
					logger.info(response.toString());
					counter.incrementAndGet();
					return response;
				}
			};
			
			tasks.add(task);
		}
		return tasks;
	}


	private void executeUseConcurrent(List<Callable<HelloResponse>> tasks) {
		ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_SIZE);
		List<Future<HelloResponse>> futures = new ArrayList<Future<HelloResponse>>(RUN_TIMES);

		for (Callable<HelloResponse> task : tasks) {
			Future<HelloResponse> future = executor.submit(task);
			futures.add(future);
		}
		for (Future<HelloResponse> future : futures) {
			try {
				future.get(1000, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
		
		logger.info("Successful task count : " + counter.get());
	}

	private void execute(List<Callable<HelloResponse>> tasks) {
		for (Callable<HelloResponse> task : tasks) {
			try {
				task.call();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
	}
	

}
