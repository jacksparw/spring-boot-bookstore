package com.assignment.bookstore.beans.dto.book;

import com.assignment.bookstore.beans.dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookAuthorDTO {
    private AuthorDTO author;
    private List<BookDTO> books;
}
