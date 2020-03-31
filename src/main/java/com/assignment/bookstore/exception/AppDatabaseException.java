package com.assignment.bookstore.exception;

public class AppDatabaseException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AppDatabaseException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public AppDatabaseException(String errorMessage) {
		super(errorMessage);
	}
}
