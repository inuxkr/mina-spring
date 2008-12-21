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
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author politics wang
 * @since Dec 10, 2008
 *
 */
public class NioSocketAcceptorFactoryBean implements FactoryBean {

	private static Logger logger = LoggerFactory.getLogger(NioSocketAcceptorFactoryBean.class);

	public static final int DEFAULT_PORT = 8012;
	public static final int DEFAULT_READ_BUFFER_SIZE = 2048;
	public static final int DEFAULT_IDLE_TIME = 10;

	private NioSocketAcceptor acceptor;
	
	private int port = DEFAULT_PORT;
	
	private int readBufferSize = DEFAULT_READ_BUFFER_SIZE;
	
	private int idleTime = DEFAULT_IDLE_TIME;
	
	private Map<String, IoFilter> extraFilters = new HashMap<String, IoFilter>();
	

	@Override
	public Object getObject() throws Exception {
		acceptor = new NioSocketAcceptor();
		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		addFilters();
		configSession();
		return acceptor;
	}


	private void addFilters() {
		for (Map.Entry<String, IoFilter> entry : extraFilters.entrySet()) {
			if (logger.isInfoEnabled()) {
				logger.info("Add " + entry.getKey() + " filter...");
			}
			acceptor.getFilterChain().addLast(entry.getKey(), entry.getValue());
		}
		if (logger.isInfoEnabled()) {
			logger.info("Add codec filter...");
		}
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	}

	private void configSession() {
		acceptor.getSessionConfig().setReadBufferSize(readBufferSize);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
	}

	
	@Override
	public Class<NioSocketAcceptor> getObjectType() {
		return NioSocketAcceptor.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

	public void setExtraFilters(Map<String, IoFilter> extraFilters) {
		this.extraFilters = extraFilters;
	}
	
	

}
