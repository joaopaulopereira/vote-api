package com.voteapp.api.service.exception;

public class OneCPFVoteSurveyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public OneCPFVoteSurveyException(String msg) {
		super(msg);
	}
	
	public OneCPFVoteSurveyException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
