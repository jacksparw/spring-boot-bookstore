package com.assignment.bookstore.service;

import com.assignment.bookstore.beans.domain.Author;
import com.assignment.bookstore.beans.domain.Book;
import com.assignment.bookstore.beans.domain.Stock;
import com.assignment.bookstore.beans.dto.AuthorDTO;
import com.assignment.bookstore.beans.dto.MediaCoverage;
import com.assignment.bookstore.beans.dto.book.BookDTO;
import com.assignment.bookstore.beans.dto.book.BookRequestDTO;
import com.assignment.bookstore.beans.dto.book.BookResponseDTO;
import com.assignment.bookstore.beans.dto.mapper.AuthorMapper;
import com.assignment.bookstore.beans.dto.mapper.BookMapper;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.exception.ValidationException;
import com.assignment.bookstore.repository.AuthorRepository;
import com.assignment.bookstore.repository.BookRepository;
import com.assignment.bookstore.repository.specification.BookAuthorSpec;
import com.assignment.bookstore.repository.specification.BookISBNSpec;
import com.assignment.bookstore.repository.specification.BookTitleSpec;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.BOOK_ALREADY_PRESENT;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.BOOK_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Log4j2
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
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addBook(BookRequestDTO bookRequest) {

        log.debug("BookService addBook Called");

        AuthorDTO authorDTO = bookRequest.getAuthorDTO();

        Optional<Author> optionalAuthor =authorRepository
                .findAuthorByAuthorName(authorDTO.getAuthorName());

        Author author;

        if(optionalAuthor.isPresent()){
            author = optionalAuthor.get();
        }else{
            log.debug("BookService addBook - Creating New Author");
            author = authorRepository.save(authorMapper.authorDtoToAuthor(authorDTO));
        }

        Optional<Book> optionalBook = bookRepository.findBookByTitleOrIsbn(bookRequest.getTitle(), bookRequest.getIsbn());

        if (optionalBook.isPresent()) {
            throw new ValidationException(BOOK_ALREADY_PRESENT);
        }

        log.debug("BookService addBook - Saving Book");

        Book book = bookMapper.bookDtoToBook(bookRequest);
        book.setAuthor(author);
        book.setStock(new Stock(bookRequest.getCount()));
        bookRepository.save(book);

        log.debug("BookService addBook - Book saved successfully");
    }

    @Override
    public List<BookResponseDTO> getBooks() {

        log.debug("BookService fetching books from DB");

        List<Book> bookList = bookRepository.findAll();

        return convertToBookAuthorDTOList(bookList);
    }

    @Override
    public List<BookResponseDTO> searchBooks(String title, String author, String isbn) {

        Specification<Book> bookSpecification =
                Specification.where(new BookAuthorSpec(author))
                        .and(new BookTitleSpec(title))
                        .and(new BookISBNSpec(isbn));

        List<Book> bookList = bookRepository.findAll(bookSpecification);

        return convertToBookAuthorDTOList(bookList);
    }

    private List<BookResponseDTO> convertToBookAuthorDTOList(List<Book> bookList) {

        log.debug("convertToBookAuthorDTOList conversion start");

        if (bookList == null || bookList.size() == 0)
            throw new NoDataFoundException(BOOK_NOT_FOUND);

        Map<AuthorDTO, List<BookDTO>> mapping = bookList.parallelStream()
                .collect(Collectors.groupingBy(
                        book -> authorMapper.authorToAuthorDTO(book.getAuthor()),
                        Collectors.mapping(bookMapper::bookToBookDTO,
                                toList())));

        List<BookResponseDTO> finalList = new ArrayList<>();

        mapping.forEach(
                (authorDTO, books) ->
                        finalList.add(new BookResponseDTO(authorDTO, books)));

        log.debug("convertToBookAuthorDTOList conversion complete");

        return finalList;
    }

    @Override
    @HystrixCommand(ignoreExceptions = {NoDataFoundException.class},
            fallbackMethod = "getFallbackMediaCoverage",
    commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "7000")
    })
    public List<MediaCoverage> searchMediaCoverage(String isbn) {

        log.debug("BookService searchMediaCoverage called");

        Book book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new NoDataFoundException(BOOK_NOT_FOUND));

        log.debug("BookService searchMediaCoverage calling media coverage service");

        ResponseEntity<MediaCoverage[]> response = restTemplate.getForEntity(mediaURL, MediaCoverage[].class);

        log.debug("BookService searchMediaCoverage got response from service with status code "+response.getStatusCode());

        List<MediaCoverage> mediaCoverages = Arrays.asList(response.getBody())
                .parallelStream()
                .filter(e -> e.getTitle().contains(book.getTitle()) || e.getBody().contains(book.getTitle()))
                .collect(toList());

        if(mediaCoverages.size() < 1)
            throw new NoDataFoundException("Media Coverage not found");

        return mediaCoverages;
    }

    public List<MediaCoverage> getFallbackMediaCoverage(String title) {

        log.debug("BookService searchMediaCoverage Histrix fallback method called");

        return Arrays.asList(new MediaCoverage(null,null,title,"dummy body"));
    }
}
