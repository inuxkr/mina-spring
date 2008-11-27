package org.springframework.remoting.mina.gettingstarted.config;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.mina.gettingstarted.AbstractHelloServiceClientTests;
import org.springframework.remoting.mina.gettingstarted.HelloService;


/**
 *
 * @autoor politics wang`
 * @since 2008-11-27
 *
 */
public class SpringHelloServiceClientTest extends AbstractHelloServiceClientTests {

	@Test
	public void test() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("hello-service-client.xml", getClass());
		String beanName = "helloService";
		HelloService service = (HelloService) context.getBean(beanName);
		invokeService(service);
		((DisposableBean) context.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName)).destroy();
	}
	
}
