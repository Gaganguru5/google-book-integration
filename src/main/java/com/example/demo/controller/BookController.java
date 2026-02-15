package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.db.Book;
import com.example.demo.google.GoogleBook;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private static final Logger log =
            LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        log.info("GET /books - Request received");

        List<Book> books = bookService.getAllBooks();

        log.info("GET /books - Returning {} books", books.size());

        return ResponseEntity.ok(books);
    }

    @GetMapping("/google")
    public ResponseEntity<GoogleBook> searchGoogleBooks(
            @RequestParam("q") String query,
            @RequestParam(value = "maxResults", required = false) Integer maxResults,
            @RequestParam(value = "startIndex", required = false) Integer startIndex) {

        log.info("GET /books/google - Request received");
        log.info("Query: {}, maxResults: {}, startIndex: {}",
                query, maxResults, startIndex);

        GoogleBook result =
                bookService.searchGoogleBooks(query, maxResults, startIndex);

        log.info("GET /books/google - Search completed successfully");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{googleId}")
    public ResponseEntity<Book> createBookFromGoogle(
            @PathVariable String googleId) {

        log.info("POST /books/{} - Request received", googleId);

        Book savedBook =
                bookService.importBookFromGoogle(googleId);

        log.info("POST /books/{} - Book created successfully with DB id: {}",
                googleId, savedBook.getId());

        return ResponseEntity.status(201).body(savedBook);
    }
}
