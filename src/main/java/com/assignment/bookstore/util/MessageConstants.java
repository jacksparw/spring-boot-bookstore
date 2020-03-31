package com.assignment.bookstore.util;

public interface MessageConstants {

	interface RequestStatus{
		String STATUS_FAILURE = "failure";
		String STATUS_SUCCESS = "success";
	}

	interface SuccessMessage{
		String BOOK_DETAILS = "Book Details";
		String MEDIA_COVERAGE_DETAILS = "Media Coverage Details";
		String BOOK_ADDED = "Book Added";
		String PURCHASE_DETAILS = "Purchase Details";
	}

	interface ErrorMessage {
		String GENERIC_ERROR_MESSAGE = "some error has occurred, please try after sometime";
		String INVALID_MESSAGE = "invalid message";
		String TITLE_IS_MANDATORY = "title is mandatory";
		String ISBN_IS_MANDATORY = "isbn is mandatory";
		String INVALID_PRICE = "invalid price must be greater than zero";
		String SEARCH_PARAMETER_MISSING = "please provide title or author or isbn as search parameter";
		String CUSTOMER_NOT_FOUND = "customer not found";
		String BOOK_NOT_FOUND = "Book Not Found";
	}
}
