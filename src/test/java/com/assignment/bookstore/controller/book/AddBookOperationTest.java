package com.assignment.bookstore.controller.book;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.assignment.bookstore.TestUtil;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.controller.BookController;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@WebMvcTest(BookController.class)
class AddBookOperationTest {

    private @MockBean BookService bookService;
    private @Autowired WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private BookDTO validBookDTO;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {

        validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("1")
                .build();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(JacksonResultHandlers.prepareJackson(TestUtil.mapper))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080)
                        .and().snippets()
                        .withDefaults(CliDocumentation.curlRequest(),
                                HttpDocumentation.httpRequest(),
                                HttpDocumentation.httpResponse(),
                                AutoDocumentation.requestFields(),
                                AutoDocumentation.responseFields(),
                                AutoDocumentation.pathParameters(),
                                AutoDocumentation.requestParameters(),
                                AutoDocumentation.description(),
                                AutoDocumentation.methodAndPath(),
                                AutoDocumentation.section()))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    /**
     *
     * @title This is my custom title
     */
    @Test
    void testAddBook_InvalidJsonRequestBody() throws Exception {
        mockMvc.perform(
                post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{This is not valid Json should fail][/}"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("invalid message"));
    }


    @Test
    void testAddBook_MissingRequestBody() throws Exception {
        mockMvc.perform(
                post("/book"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Book Added"));
    }
}