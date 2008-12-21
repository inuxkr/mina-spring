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

package org.springframework.remoting.mina;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 */
public class DefaultMinaRequestExecutor implements MinaRequestExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultMinaRequestExecutor.class);

	public static final long DEFAULT_RECOVERY_INTERVAL = 3000L;
	
	
	private IoConnector connector;
	
	private IoSession session;
	
	private MinaClientConfiguration configuration;

	private ResultReceiver resultReceiver;
	
	private Lock lock = new ReentrantLock();
	
	private long recoveryInterval = DEFAULT_RECOVERY_INTERVAL;

	private AtomicBoolean running = new AtomicBoolean(true);

	private AtomicBoolean connected = new AtomicBoolean(false);
	
	
	@Override
	public ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) 
		throws Exception {
		
		if (!isConnected()) {
			return handleDisconnected(invocation);
		}
		
		WriteFuture writeFuture = session.write(invocation);
		writeFuture.awaitUninterruptibly();
		return resultReceiver.takeResult(invocation.getReturnAddress());
	}

	private ReturnAddressAwareRemoteInvocationResult handleDisconnected(ReturnAddressAwareRemoteInvocation invocation) {
		Exception e = new SocketException("Client disconnected");
		return new ReturnAddressAwareRemoteInvocationResult(invocation.getReturnAddress(), e );
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(connector, "connector required");
		Assert.notNull(resultReceiver, "resultReceiver required");
		initialize();
	}

	public void initialize() {
		connector.setHandler(new MinaClientHandler(resultReceiver, this));
		connect();		
	}

	@Override
	public void connect() {
		lock.lock();
		while (isRunning()) {
			resultReceiver.interrupt();
			try {
				ConnectFuture future = connector.connect(getAddress());
				future.awaitUninterruptibly();
				session = future.getSession();
				connected.set(true);
				logger.info("Successfully connect to " + getAddress());
				break;
			} catch(Exception e) {
				connected.set(false);
				logger.error("Couldn't connect to " + getAddress() + e.getMessage() + ", retrying in " + recoveryInterval  + " ms", e);
			}	

			sleepInbetweenRecoveryAttempts();
		}
		lock.unlock();
	}

	private boolean isRunning() {
		return running.get();
	}

	private boolean isConnected() {
		return connected.get();
	}
	
	/**
	 * Sleep according to the specified recovery interval.
	 * Called inbetween recovery attempts.
	 */
	protected void sleepInbetweenRecoveryAttempts() {
		if (this.recoveryInterval > 0) {
			try {
				Thread.sleep(this.recoveryInterval);
			}
			catch (InterruptedException interEx) {
				// Re-interrupt current thread, to allow other threads to react.
				Thread.currentThread().interrupt();
			}
		}
	}

	private InetSocketAddress getAddress() {
		return new InetSocketAddress(configuration.getHostName(), configuration.getPort());
	}

	@Override
	public void destroy() throws Exception {
		lock.lock();
		running.set(false);
		connected.set(false);
		session.close(false).awaitUninterruptibly();
		connector.dispose();
		lock.unlock();
	}

	@Override
	public void setMinaClientConfiguration(MinaClientConfiguration configuration) {
		this.configuration = configuration;
	}

	public void setResultReceiver(ResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public void setConnector(IoConnector connector) {
		this.connector = connector;
	}

	public void setRecoveryInterval(long recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
	}
	
	

}
