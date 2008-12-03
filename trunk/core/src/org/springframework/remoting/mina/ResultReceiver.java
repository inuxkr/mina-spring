package org.springframework.remoting.mina;

public interface ResultReceiver {

	void resultReceived(ReturnAddressAwareRemoteInvocationResult result);

	ReturnAddressAwareRemoteInvocationResult getResult(ReturnAddress returnAddress);

}
