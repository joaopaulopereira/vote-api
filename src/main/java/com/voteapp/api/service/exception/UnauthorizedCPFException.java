package com.voteapp.api.service.exception;

public class UnauthorizedCPFException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedCPFException(String msg) {
		super(msg);
	}
	
	public UnauthorizedCPFException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
