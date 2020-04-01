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
		String LIMITED_OR_OUT_OF_STOCK = "some books in order have limited or out of stock";
		String BOOK_ALREADY_PRESENT = "Book Already Present with given title or ISBN";
		String AUTHOR_NOT_FOUND = "Author not found";
		String PRICE_IS_MISSING = "Price is missing";
		String AUTHOR_ID_MISSING = "authorId missing";
		String INVALID_AUTHOR_ID = "invalid authorId";
		String STOCK_COUNT_MISSING = "stock count missing";
		String INVALID_STOCK = "invalid stock";
		String CUSTOMER_ID_IS_MANDATORY = "customer id is mandatory";
		String CUSTOMER_ID_IS_INVALID = "customer id is invalid";
		String BOOK_ID_IS_MISSING = "book id is missing";
		String INVALID_BOOK_ID = "invalid bookId";
		String QUANTITY_IS_MISSING = "quantity is missing";
		String INVALID_QUANTITY = "invalid quantity must be greater than zero";
	}
}
