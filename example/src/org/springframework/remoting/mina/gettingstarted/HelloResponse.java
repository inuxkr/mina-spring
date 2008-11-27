package org.springframework.remoting.mina.gettingstarted;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class HelloResponse implements Serializable {

	private static final long serialVersionUID = 1861076732864361024L;
	
	private int sequence;

	public void setSequence(int counter) {
		this.sequence = counter;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
