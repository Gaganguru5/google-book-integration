package com.example.demo.service;

import java.util.List;

import com.example.demo.db.Book;
import com.example.demo.google.GoogleBook;

public interface BookService {

    List<Book> getAllBooks();

    GoogleBook searchGoogleBooks(String query,
                                  Integer maxResults,
                                  Integer startIndex);

    Book importBookFromGoogle(String googleId);
}
