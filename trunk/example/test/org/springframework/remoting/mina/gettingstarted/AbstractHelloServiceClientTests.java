package org.springframework.remoting.mina.gettingstarted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
	private static final int RUN_TIMES = 1000;

	protected void invokeService(final HelloService service) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_SIZE);
		
		Collection<Callable<HelloResponse>> tasks = new ArrayList<Callable<HelloResponse>>();
		
		for (int i = 0; i < RUN_TIMES; i++) {
			tasks.add(new Callable<HelloResponse>() {
				@Override
				public HelloResponse call() throws Exception {
					HelloResponse response = service.sayHello(new HelloRequest());
					return response;
				}
			});
		}
		
		List<Future<HelloResponse>> futures = executor.invokeAll(tasks);
		
		for (Future<HelloResponse> future : futures) {
			logger.debug(future.get().toString());
		}
	}

}
