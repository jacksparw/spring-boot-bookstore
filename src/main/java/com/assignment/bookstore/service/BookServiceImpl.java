package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.domain.Author;
import com.assignment.bookstore.beans.domain.Book;
import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookAuthorDTO;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.beans.dto.mapper.AuthorMapper;
import com.assignment.bookstore.beans.dto.mapper.BookMapper;
import com.assignment.bookstore.beans.dto.MediaCoverage;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.repository.AuthorRepository;
import com.assignment.bookstore.repository.BookRepository;
import com.assignment.bookstore.repository.specification.BookAuthorSpec;
import com.assignment.bookstore.repository.specification.BookISBNSpec;
import com.assignment.bookstore.repository.specification.BookTitleSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.BOOK_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final RestTemplate restTemplate;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    @Value("${app.media.coverage.URI}")
    private URI mediaURL;

    public BookServiceImpl(BookRepository repository, AuthorRepository authorRepository, RestTemplate restTemplate, BookMapper bookMapper, AuthorMapper authorMapper) {
        this.bookRepository = repository;
        this.authorRepository = authorRepository;
        this.restTemplate = restTemplate;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @Override
    public void addBook(BookRequestDTO bookRequest) {

        Author author = authorRepository
                .findById(bookRequest.getAuthorId())
                .orElseThrow(RuntimeException::new);

        Book book = bookMapper.bookDtoToBook(bookRequest);
        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Override
    public List<BookAuthorDTO> getBooks() {

        List<Book> bookList = bookRepository.findAll();

        return convertToBookAuthorDTOList(bookList);
    }

    @Override
    public List<BookAuthorDTO> searchBooks(String title, String author, String isbn) {

        Specification<Book> bookSpecification =
                Specification.where(new BookAuthorSpec(author))
                        .and(new BookTitleSpec(title))
                        .and(new BookISBNSpec(isbn));

        List<Book> bookList = bookRepository.findAll(bookSpecification);

        return convertToBookAuthorDTOList(bookList);
    }

    private List<BookAuthorDTO> convertToBookAuthorDTOList(List<Book> bookList) {
        if (bookList == null || bookList.size() == 0)
            throw new NoDataFoundException(BOOK_NOT_FOUND);

        Map<AuthorDTO, List<BookDTO>> mapping = bookList.parallelStream()
                .collect(Collectors.groupingBy(
                        book -> authorMapper.authorToAuthorDTO(book.getAuthor()),
                        Collectors.mapping(bookMapper::bookToBookDTO,
                                toList())));

        List<BookAuthorDTO> finalList = new ArrayList<>();

        mapping.forEach(
                (authorDTO, books) ->
                        finalList.add(new BookAuthorDTO(authorDTO, books)));

        return finalList;
    }

    @Override
    public List<MediaCoverage> searchMediaCoverage(String title) {
        ResponseEntity<MediaCoverage[]> response = restTemplate.getForEntity(mediaURL, MediaCoverage[].class);

        return Arrays.asList(response.getBody())
                .parallelStream()
                .filter(e -> e.getTitle().contains(title) || e.getBody().contains(title))
                .collect(toList());
    }
}
