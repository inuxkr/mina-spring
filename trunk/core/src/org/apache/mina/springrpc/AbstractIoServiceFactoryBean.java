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

package org.apache.mina.springrpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author politics wang
 * @since Dec 22, 2008
 *
 */
public abstract class AbstractIoServiceFactoryBean implements FactoryBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Map<String, IoFilter> extraFilters = new HashMap<String, IoFilter>();

	protected void configFilters(IoService service) {
		for (Map.Entry<String, IoFilter> entry : extraFilters.entrySet()) {
			if (logger.isInfoEnabled()) {
				logger.info("Add filter " + entry.getKey() + "...");
			}
			service.getFilterChain().addLast(entry.getKey(), entry.getValue());
		}
		if (logger.isInfoEnabled()) {
			logger.info("Add codec filter...");
		}
		service.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
	}


	public void setExtraFilters(Map<String, IoFilter> extraFilters) {
		this.extraFilters = extraFilters;
	}

}
