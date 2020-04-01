package com.assignment.bookstore.repository;

import com.assignment.bookstore.beans.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {

    Optional<Book> findBookByTitleOrIsbn(String title, String isbn);
}
