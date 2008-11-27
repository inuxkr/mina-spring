package org.springframework.remoting.mina.gettingstarted;

import java.util.concurrent.atomic.AtomicInteger;


public class DefaultHelloService implements HelloService {

	private static AtomicInteger counter = new AtomicInteger();
	
	@Override
	public HelloResponse sayHello(HelloRequest helloRequest) {
		HelloResponse helloResponse = new HelloResponse();
		helloResponse.setSequence(counter.incrementAndGet());
		return helloResponse;
	}

}
