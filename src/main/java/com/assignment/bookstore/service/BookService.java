package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.book.BookAuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.beans.dto.MediaCoverage;

import java.util.List;

public interface BookService {

	void addBook(BookRequestDTO book);
	List<BookAuthorDTO> getBooks();
	List<MediaCoverage> searchMediaCoverage(String title);
    List<BookAuthorDTO> searchBooks(String title, String author, String isbn);
}
