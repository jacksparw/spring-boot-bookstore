package com.assignment.bookstore.controller.book;

import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookResponseDTO;
import com.assignment.bookstore.controller.BookController;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class SearchBookOperationTest {

    private @Autowired MockMvc mockMvc;
    private @MockBean BookService bookService;
    private BookDTO validBookDTO;
    private AuthorDTO validAuthorDTO;


    @BeforeEach
    public void setup() {

        validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("1")
                .build();

        validAuthorDTO = AuthorDTO.builder()
                .authorName("Dummy Author")
                .description("This is Dummy Author")
                .build();
    }

    @Test
    void testSearchBookAllSearchParameterMissing() throws Exception {

        mockMvc.perform(get("/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("please provide title or author or isbn as search parameter"));
    }

    @Test
    void testSearchBookAllowIfAllSearchParameterNotMissing() throws Exception {

        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(validBookDTO);

        BookResponseDTO responseDTO = new BookResponseDTO(validAuthorDTO, bookDTOList);

        Mockito.when(bookService.searchBooks("Dummy Book", null, null)).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(
                get("/search")
                        .param("title", "Dummy Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Book Details"));
    }

    @Test
    void testSearchBook_NoBooks() throws Exception {

        Mockito.when(bookService.searchBooks("dummy", null, null)).thenThrow(new NoDataFoundException("No Book Found"));

        mockMvc.perform(
                get("/search")
                        .param("title", "dummy"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("No Book Found"));
    }

    @Test
    void testSearchBook() throws Exception {

        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(validBookDTO);

        BookResponseDTO responseDTO = new BookResponseDTO(validAuthorDTO, bookDTOList);

        Mockito.when(bookService.searchBooks("Dummy Book", "Dummy Author", "1")).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(
                get("/search")
                        .param("title", "Dummy Book")
                        .param("author", "Dummy Author")
                        .param("isbn", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Book Details"))
                .andExpect(jsonPath("$.data[:1].author.name").value("Dummy Author"))
                .andExpect(jsonPath("$.data[:1].books[:1].title").value("DummyBook"));
    }
}