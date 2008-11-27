package org.springframework.remoting.mina.gettingstarted.coding;


import org.junit.Test;
import org.springframework.remoting.mina.MinaProxyFactoryBean;
import org.springframework.remoting.mina.MinaServiceExporter;
import org.springframework.remoting.mina.gettingstarted.AbstractHelloServiceClientTests;
import org.springframework.remoting.mina.gettingstarted.HelloService;


/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class HelloServiceClientTest extends AbstractHelloServiceClientTests {
	
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
	
}
