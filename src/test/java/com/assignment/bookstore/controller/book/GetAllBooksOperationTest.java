package com.assignment.bookstore.controller.book;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.assignment.bookstore.TestUtil;
import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookResponseDTO;
import com.assignment.bookstore.controller.BookController;
import com.assignment.bookstore.exception.NoDataFoundException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@WebMvcTest(BookController.class)
class GetAllBooksOperationTest {

    private @MockBean BookService bookService;
    private @Autowired WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private BookDTO validBookDTO;
    private AuthorDTO validAuthorDTO;


    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation){

        validBookDTO = BookDTO.builder()
                .title("DummyBook")
                .price(BigDecimal.TEN)
                .isbn("1")
                .build();

        validAuthorDTO = AuthorDTO.builder()
                    .authorName("Dummy Author")
                    .description("This is Dummy Author")
                    .build();

        mockMvc =  MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(JacksonResultHandlers.prepareJackson(TestUtil.mapper))
                .alwaysDo(MockMvcRestDocumentation.document("{method-name}",
                        Preprocessors.preprocessRequest(),
                        Preprocessors.preprocessResponse(
                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                ResponseModifyingPreprocessors.limitJsonArrayLength(TestUtil.mapper),
                                Preprocessors.prettyPrint())))
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
                .build();
    }

    @Test
    void testGetAllBook() throws Exception {

        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(validBookDTO);

        BookResponseDTO responseDTO = new BookResponseDTO(validAuthorDTO, bookDTOList);

        Mockito.when(bookService.getBooks()).thenReturn(Arrays.asList(responseDTO));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Book Details"))
                .andExpect(jsonPath("$.data[:1].author.authorName").value("Dummy Author"))
                .andExpect(jsonPath("$.data[:1].books[:1].title").value("DummyBook"));
    }


    @Test
    void testGetAllBook_NoBooks() throws Exception {
        Mockito.when(bookService.getBooks()).thenThrow(new NoDataFoundException("No Book Found"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("failure"))
                .andExpect(jsonPath("$.message").value("No Book Found"));
    }
}