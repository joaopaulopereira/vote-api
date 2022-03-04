package com.voteapp.api.service.exception;

public class ExternalServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ExternalServiceException(String msg) {
		super(msg);
	}
	
	public ExternalServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
