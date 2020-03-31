package com.assignment.bookstore.beans.dto.mapper;

import com.assignment.bookstore.beans.domain.Book;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {

    BookDTO bookToBookDTO(Book book);
    Book bookDtoToBook(BookDTO dto);
}
