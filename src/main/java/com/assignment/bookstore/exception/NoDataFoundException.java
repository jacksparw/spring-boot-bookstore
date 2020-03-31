package com.assignment.bookstore.exception;

public class NoDataFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NoDataFoundException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public NoDataFoundException(String errorMessage) {
		super(errorMessage);
	}
}
