package org.springframework.cloud.servicebroker.hawqdb.exception;

import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;


public class HawqServiceException extends ServiceBrokerException {

	private static final long serialVersionUID = 8667141725171626000L;

	public HawqServiceException(String message) {
		super(message);
	}
	
}
