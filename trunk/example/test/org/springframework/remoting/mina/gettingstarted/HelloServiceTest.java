package org.springframework.remoting.mina.gettingstarted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.mina.MinaProxyFactoryBean;
import org.springframework.remoting.mina.MinaServiceExporter;


/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class HelloServiceTest {
	
	private static Logger logger = LoggerFactory.getLogger(HelloServiceTest.class);
	
	@Test
	public void test() throws Exception {
		MinaProxyFactoryBean bean = new MinaProxyFactoryBean();
		bean.setServiceUrl("tcp://localhost:" + MinaServiceExporter.DEFAULT_PORT);
		bean.setServiceInterface(HelloService.class);
		bean.afterPropertiesSet();
		HelloService service = (HelloService) bean.getObject();
		invokeService(service);
		bean.destroy();
	}

	private void invokeService(final HelloService service) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		Collection<Callable<HelloResponse>> tasks = new ArrayList<Callable<HelloResponse>>();
		
		for (int i = 0; i < 1000; i++) {
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
