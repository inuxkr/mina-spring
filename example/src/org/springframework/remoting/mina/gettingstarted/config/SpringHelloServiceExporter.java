package org.springframework.remoting.mina.gettingstarted.config;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @autoor politics wang`
 * @since 2008-11-27
 *
 */
public class SpringHelloServiceExporter {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("hello-service-exporter.xml", SpringHelloServiceExporter.class);
	}

}
