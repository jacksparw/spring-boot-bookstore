package com.assignment.bookstore.controller;

import com.assignment.bookstore.beans.dto.GenericResponseDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.BookService;
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

@RestController
public class BookController {

	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(value = "${paths.getBooks}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> getAllBook() {

		GenericResponseDTO response = createSuccessResponse(BOOK_DETAILS);
		response.setData(bookService.getBooks());

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "${paths.searchBook}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> searchBook(@RequestParam(required = false) String title,
														 @RequestParam(required = false) String author,
														 @RequestParam(required = false) String isbn) {

		if(StringUtils.isEmpty(title) && StringUtils.isEmpty(author) && StringUtils.isEmpty(isbn))
			throw new ValidationException(SEARCH_PARAMETER_MISSING);


		GenericResponseDTO response = createSuccessResponse(BOOK_DETAILS);
		response.setData(bookService.searchBooks(title, author, isbn));

		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "${paths.addBook}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> addBook(@RequestBody @Valid BookRequestDTO book, BindingResult result) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				throw new ValidationException(error.getDefaultMessage());
			}
		}

		bookService.addBook(book);

		return ResponseEntity.ok(createSuccessResponse(BOOK_ADDED));
	}

	@GetMapping(value = "${paths.search.mediaCoverage}", produces = APPLICATION_JSON)
	public ResponseEntity<GenericResponseDTO> searchMediaCoverage(@RequestParam(name="isbn") String isbn) {

		if(StringUtils.isEmpty(isbn))
			throw new ValidationException(ISBN_IS_MANDATORY);

		GenericResponseDTO response = createSuccessResponse(MEDIA_COVERAGE_DETAILS);
		response.setData(bookService.searchMediaCoverage(isbn));

		return ResponseEntity.ok(response);
	}

	private static GenericResponseDTO createSuccessResponse(String s) {
		GenericResponseDTO response = new GenericResponseDTO();
		response.setMessage(s);
		response.setStatus(STATUS_SUCCESS);
		return response;
	}
}
