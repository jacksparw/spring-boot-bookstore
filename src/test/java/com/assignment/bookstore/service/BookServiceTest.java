package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.beans.dto.book.BookResponseDTO;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTest {

    private @Autowired BookService bookService;

    private BookDTO validBookDTO;
    private AuthorDTO validAuthorDTO;

    @BeforeEach
    public void setUp() {
        validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("11")
                .build();

        validAuthorDTO = AuthorDTO.builder()
                .authorName("DummyAuthor")
                .description("This is a dummy Author")
                .build();
    }

    @Test
    @Transactional
    public void testAddBook() {

        //given
        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorDTO(validAuthorDTO);
        bookRequestDto.setCount(10);

        //when
        bookService.addBook(bookRequestDto);
    }

    @Test
    @Transactional
    public void testAddBook_BookAlreadyPresent() {

        //given
        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorDTO(validAuthorDTO);
        bookRequestDto.setCount(10);

        //When
        bookService.addBook(bookRequestDto);
        Exception exception = assertThrows(ValidationException.class, () -> bookService.addBook(bookRequestDto));

        //then
        String expectedMessage = "Book Already Present with given title or ISBN";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetBooks() {

        //when
        List<BookResponseDTO> books = bookService.getBooks();

        //then
        assertNotNull(books);
        assertTrue(books.size() > 0);
    }

    @Test
    public void searchBooks_With_TitleAndAuthAndISBN() {

        //when
        List<BookResponseDTO> books = bookService.searchBooks("Meluha","Amish","1");

        //then
        assertNotNull(books);
        assertTrue(books.size() == 1);
    }

    @Test
    public void searchBooks_With_ISBN() {

        //when
        List<BookResponseDTO> books = bookService.searchBooks(null,null,"1");

        //then
        assertNotNull(books);
        assertTrue(books.get(0).getBooks().size() == 1);
    }

    @Test
    public void searchBooks_With_Title_FuzzyMatch() {

        //when
        List<BookResponseDTO> books = bookService.searchBooks("Spring",null,null);

        //then
        assertNotNull(books);
        assertTrue(books.get(0).getBooks().size() == 2);
    }

    @Test
    public void searchBooks_With_Author_FuzzyMatch() {

        //when
        List<BookResponseDTO> books = bookService.searchBooks(null,"Long",null);

        //then
        assertNotNull(books);
        assertTrue(books.get(0).getBooks().size() == 3);
    }

    @Test
    public void searchBooks_With_AuthorAndTitle_FuzzyMatch() {

        //when
        List<BookResponseDTO> books = bookService.searchBooks("Integr","Long",null);

        //then
        assertNotNull(books);
        assertTrue(books.get(0).getBooks().size() == 1);
    }

    @Test
    public void searchBooks_NoBookFound() {

        //when
        Exception exception = assertThrows(NoDataFoundException.class, () ->bookService.searchBooks("Integr","Long","999"));

        //then
        String expectedMessage = "Book Not Found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
