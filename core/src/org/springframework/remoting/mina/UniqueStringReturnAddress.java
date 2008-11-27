package org.springframework.remoting.mina;

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author politics wang
 * @since 2008-11-25
 * 
 */
public class UniqueStringReturnAddress implements ReturnAddress {

	private static final long serialVersionUID = -3771718276168163527L;
	
	private String unique;
	
	public UniqueStringReturnAddress() {
		unique = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	
	
}
