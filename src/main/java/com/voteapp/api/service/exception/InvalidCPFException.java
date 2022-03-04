package com.voteapp.api.service.exception;

public class InvalidCPFException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidCPFException(String msg) {
		super(msg);
	}
	
	public InvalidCPFException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
