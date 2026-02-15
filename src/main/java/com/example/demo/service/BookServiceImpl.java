package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.db.Book;
import com.example.demo.db.BookRepository;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GoogleBookService googleBookService;

    private static final Logger log =
            LoggerFactory.getLogger(BookServiceImpl.class);

    public BookServiceImpl(BookRepository bookRepository,
                           GoogleBookService googleBookService) {
        this.bookRepository = bookRepository;
        this.googleBookService = googleBookService;
    }

    @Override
    public List<Book> getAllBooks() {

        log.info("Fetching all books from database");

        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            log.warn("No books found in database");
            throw new BookNotFoundException("No books found in database");
        }

        return books;
    }

    @Override
    public GoogleBook searchGoogleBooks(String query,
                                        Integer maxResults,
                                        Integer startIndex) {

        log.info("Searching Google Books with query: {}", query);

        return googleBookService.searchBooks(query, maxResults, startIndex);
    }

    @Override
    public Book importBookFromGoogle(String googleId) {

        log.info("Importing book from Google API with id: {}", googleId);

        GoogleBook.Item googleItem =
                googleBookService.getBookById(googleId);

        GoogleBook.Item item = Optional.ofNullable(googleItem)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found in Google API"));

        GoogleBook.VolumeInfo volumeInfo =
                Optional.ofNullable(item.volumeInfo())
                        .orElseThrow(() ->
                                new BookNotFoundException("Volume info missing"));

        String title = volumeInfo.title();

        String author = Optional.ofNullable(volumeInfo.authors())
                .filter(authors -> !authors.isEmpty())
                .map(authors -> authors.get(0))
                .orElse("Unknown Author");

        Integer pageCount = volumeInfo.pageCount();

        Book book = new Book(googleId, title, author);
        book.setPageCount(pageCount);

        Book savedBook = bookRepository.save(book);

        log.info("Book saved successfully with id: {}", savedBook.getId());

        return savedBook;
    }
}
