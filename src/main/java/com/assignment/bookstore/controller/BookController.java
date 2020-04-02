package com.assignment.bookstore.controller;

import com.assignment.bookstore.beans.dto.GenericResponseDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.ISBN_IS_MANDATORY;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.SEARCH_PARAMETER_MISSING;
import static com.assignment.bookstore.util.MessageConstants.RequestStatus.STATUS_SUCCESS;
import static com.assignment.bookstore.util.MessageConstants.SuccessMessage.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Log4j2
@RestController
public class BookController {

	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(value = "${paths.getBooks}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> getAllBook() {

		log.debug("BookController getAllBook called");

		GenericResponseDTO response = createSuccessResponse(BOOK_DETAILS);
		response.setData(bookService.getBooks());

		log.debug("BookController getAllBook successful");

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "${paths.searchBook}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> searchBook(@RequestParam(required = false) String title,
														 @RequestParam(required = false) String author,
														 @RequestParam(required = false) String isbn) {

		if(StringUtils.isEmpty(title) && StringUtils.isEmpty(author) && StringUtils.isEmpty(isbn))
			throw new ValidationException(SEARCH_PARAMETER_MISSING);

		log.debug(String.format("BookController searchBook validation successful with parameters title %s Author %s ISBN %s", title, author, isbn));

		GenericResponseDTO response = createSuccessResponse(BOOK_DETAILS);
		response.setData(bookService.searchBooks(title, author, isbn));

		log.debug("BookController searchBook Successful");

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "${paths.addBook}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> addBook(@RequestBody @Valid BookRequestDTO book, BindingResult result) {

		log.debug("BookController addBook called");

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				throw new ValidationException(error.getDefaultMessage());
			}
		}

		log.debug(String.format("BookController addBook validation successful with ISBN %s ", book.getIsbn()));

		bookService.addBook(book);

		log.debug("BookController addBook Successful");

		return ResponseEntity.ok(createSuccessResponse(BOOK_ADDED));
	}

	@GetMapping(value = "${paths.search.mediaCoverage}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> searchMediaCoverage(@RequestParam(name="isbn") String isbn) {

		log.debug("BookController searchMediaCoverage called");

		if(StringUtils.isEmpty(isbn))
			throw new ValidationException(ISBN_IS_MANDATORY);

		log.debug(String.format("BookController searchMediaCoverage validation with isbn %s", isbn));

		GenericResponseDTO response = createSuccessResponse(MEDIA_COVERAGE_DETAILS);
		response.setData(bookService.searchMediaCoverage(isbn));

		log.debug("BookController searchMediaCoverage Successful");

		return ResponseEntity.ok(response);
	}

	private static GenericResponseDTO createSuccessResponse(String s) {
		GenericResponseDTO response = new GenericResponseDTO();
		response.setMessage(s);
		response.setStatus(STATUS_SUCCESS);
		return response;
	}
}
