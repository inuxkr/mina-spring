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

import org.apache.mina.core.service.IoConnector;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public interface MinaRequestExecutor extends InitializingBean, DisposableBean {
	
	void setMinaClientConfiguration(MinaClientConfiguration configuration);
	
	void setResultReceiver(ResultReceiver resultReceiver);

	void setConnector(IoConnector connector);

	ReturnAddressAwareRemoteInvocationResult executeRequest(ReturnAddressAwareRemoteInvocation invocation) throws Exception;

	void connect();

	boolean isRunning();
}
