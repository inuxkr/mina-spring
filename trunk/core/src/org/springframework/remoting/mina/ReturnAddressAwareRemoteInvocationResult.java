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

import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class ReturnAddressAwareRemoteInvocationResult extends
		RemoteInvocationResult implements ReturnAddressAware {

	private static final long serialVersionUID = -9151370144617233397L;
	
	private ReturnAddress returnAddress;
	
	public ReturnAddressAwareRemoteInvocationResult(ReturnAddress returnAddress, Object value) {
		super(value);
		setReturnAddress(returnAddress);
	}
	
	public ReturnAddressAwareRemoteInvocationResult(ReturnAddress returnAddress, Exception exception) {
		super(exception);
		setReturnAddress(returnAddress);
	}
	
	
	@Override
	public ReturnAddress getReturnAddress() {
		return returnAddress;
	}

	@Override
	public void setReturnAddress(ReturnAddress returnAddress) {
		this.returnAddress = returnAddress;
	}

}
