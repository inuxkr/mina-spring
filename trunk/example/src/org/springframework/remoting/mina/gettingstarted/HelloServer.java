package org.springframework.remoting.mina.gettingstarted;

import org.springframework.remoting.mina.MinaServiceExporter;


public class HelloServer {

	public static void main(String[] args) throws Exception {
		MinaServiceExporter exporter = new MinaServiceExporter();
		HelloService service = new DefaultHelloService();
		exporter.setService(service);
		exporter.setServiceInterface(HelloService.class);
		exporter.afterPropertiesSet();
	}

}
