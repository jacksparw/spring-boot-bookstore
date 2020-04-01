package com.assignment.bookstore.controller.book;

import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.controller.BookController;
import com.assignment.bookstore.TestUtil;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class AddBookOperationTest {

    private @Autowired MockMvc mockMvc;
    private @MockBean BookService bookService;
    private BookDTO validBookDTO;

    @BeforeEach
    public void setup() {

        validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("1")
                .build();
    }

    @Test
    void testAddBook_MissingRequestBody() throws Exception {
        mockMvc.perform(
                post("/book"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid message"));
    }

    @Test
    void testAddBook_InvalidJsonRequestBody() throws Exception {
        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{This is not valid Json should fail][/}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid message"));
    }

    @Test
    void testAddBook_MissingAuthorId() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setCount(10);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("authorId missing"));
    }

    @Test
    void testAddBook_InvalidAuthorId() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(-234L);
        bookRequestDto.setCount(10);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid authorId"));
    }

    @Test
    void testAddBook_missingStock() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("stock count missing"));
    }

    @Test
    void testAddBook_LessThanOneStock() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(-12);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid stock"));
    }

    @Test
    void testAddBook_InvalidTitle() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        bookRequestDto.setTitle(null);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("title is mandatory"));
    }

    @Test
    void testAddBook_InvalidISBN() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        bookRequestDto.setIsbn(null);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("isbn is mandatory"));
    }

    @Test
    void testAddBook_MissingPrice() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        bookRequestDto.setPrice(null);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("Price is missing"));
    }

    @Test
    void testAddBook_InvalidPrice() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        bookRequestDto.setPrice(new BigDecimal(-12));

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid price must be greater than zero"));
    }

    @Test
    void testAddBook_BookAlreadyPresent() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        Mockito.doThrow(new ValidationException("book already present")).when(bookService).addBook(bookRequestDto);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("book already present"));
    }

    @Test
    void testAddBook() throws Exception {

        BookRequestDTO bookRequestDto = new BookRequestDTO(validBookDTO);
        bookRequestDto.setAuthorId(1L);
        bookRequestDto.setCount(10);

        Mockito.doNothing().when(bookService).addBook(bookRequestDto);

        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJson(bookRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Book Added"));
    }
}