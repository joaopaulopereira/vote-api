package com.voteapp.api.controller.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.voteapp.api.service.exception.ExternalServiceException;
import com.voteapp.api.service.exception.InvalidCPFException;
import com.voteapp.api.service.exception.InvalidSessionException;
import com.voteapp.api.service.exception.ObjectNotFoundException;
import com.voteapp.api.service.exception.OneCPFVoteSurveyException;
import com.voteapp.api.service.exception.UnauthorizedCPFException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Object not found.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(InvalidSessionException.class)
	public ResponseEntity<StandardError> invalidSession(InvalidSessionException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Invalid session exception.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(OneCPFVoteSurveyException.class)
	public ResponseEntity<StandardError> oneCPFVoteSurvey(OneCPFVoteSurveyException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "One CPF vote survey exception.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(InvalidCPFException.class)
	public ResponseEntity<StandardError> invalidCPF(InvalidCPFException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Invalid CPF exception.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(UnauthorizedCPFException.class)
	public ResponseEntity<StandardError> unauthorizedCPF(UnauthorizedCPFException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized CPF exception.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
	}
	
	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<StandardError> externalService(ExternalServiceException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.SERVICE_UNAVAILABLE.value(), "External service unavailable. Try again.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(err);
	}
	
}

	