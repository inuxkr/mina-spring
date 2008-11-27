package org.springframework.remoting.mina.gettingstarted.coding;

import org.springframework.remoting.mina.MinaServiceExporter;
import org.springframework.remoting.mina.gettingstarted.DefaultHelloService;
import org.springframework.remoting.mina.gettingstarted.HelloService;


public class HelloServiceExporter {

	public static void main(String[] args) throws Exception {
		MinaServiceExporter exporter = new MinaServiceExporter();
		HelloService service = new DefaultHelloService();
		exporter.setService(service);
		exporter.setServiceInterface(HelloService.class);
		exporter.afterPropertiesSet();
	}

}
