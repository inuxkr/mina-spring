package org.springframework.remoting.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 */
public class DefaultMinaRequestExecutor implements MinaRequestExecutor {
	
	private IoConnector connector = new NioSocketConnector();
	
	private IoSession session;
	
	private MinaServiceClientHandler handler = new MinaServiceClientHandler();

	private MinaClientConfiguration configuration;
	
	@Override
	public ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) 
		throws Exception {
		WriteFuture writeFuture = session.write(invocation);
		writeFuture.await();
		return handler.getReceivedMessage(invocation.getReturnAddress());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialize();
	}

	public void initialize() {
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(handler);
		ConnectFuture future = connector.connect(getAddress());
		future.awaitUninterruptibly();
		session = future.getSession();		
	}

	private InetSocketAddress getAddress() {
		return new InetSocketAddress(configuration.getHostName(), configuration.getPort());
	}

	@Override
	public void destroy() throws Exception {
		try {
			session.closeOnFlush().await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		connector.dispose();
	}

	@Override
	public void setMinaClientConfiguration(MinaClientConfiguration configuration) {
		this.configuration = configuration;
	}

}
