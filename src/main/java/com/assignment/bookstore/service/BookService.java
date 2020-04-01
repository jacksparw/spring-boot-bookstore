package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.MediaCoverage;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.beans.dto.book.BookResponseDTO;

import java.util.List;

public interface BookService {

	void addBook(BookRequestDTO book);
	List<BookResponseDTO> getBooks();
	List<MediaCoverage> searchMediaCoverage(String title);
    List<BookResponseDTO> searchBooks(String title, String author, String isbn);
}
