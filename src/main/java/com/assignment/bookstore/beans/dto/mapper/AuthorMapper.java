package com.assignment.bookstore.beans.dto.mapper;

import com.assignment.bookstore.beans.domain.Author;
import com.assignment.bookstore.beans.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorMapper {

    AuthorDTO authorToAuthorDTO(Author author);
}
