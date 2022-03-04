package com.voteapp.api.service.exception;

public class InvalidSessionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidSessionException(String msg) {
		super(msg);
	}
	
	public InvalidSessionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
