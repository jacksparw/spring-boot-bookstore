package com.assignment.bookstore.beans.dto.book;

import com.assignment.bookstore.beans.dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookResponseDTO {
    private AuthorDTO author;
    private List<BookDTO> books;
}
