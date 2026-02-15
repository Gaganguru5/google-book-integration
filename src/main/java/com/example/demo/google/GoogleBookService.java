package com.example.demo.google;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GoogleBookService {

    private final RestClient restClient;
	private static final Logger log = LoggerFactory.getLogger(GoogleBookService.class);

    public GoogleBookService(
            @Value("${google.books.base-url:https://www.googleapis.com/books/v1}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public GoogleBook searchBooks(String query, Integer maxResults, Integer startIndex) {

        try {
        	log.info("Calling Google API for query: {}", query);

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/volumes")
                            .queryParam("q", query)
                            .queryParam("maxResults", Optional.ofNullable(maxResults).orElse(10))
                            .queryParam("startIndex", Optional.ofNullable(startIndex).orElse(0))
                            .build())
                    .retrieve()
                    .body(GoogleBook.class);

        } catch (Exception ex) {
        	log.error("Error while calling Google API", ex);

            throw new ExternalServiceException("Failed to fetch books from Google API");
        }
    }

    public GoogleBook.Item getBookById(String id) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/volumes/{id}").build(id))
                    .retrieve()
                    .body(GoogleBook.Item.class);

        } catch (Exception ex) {
            throw new ExternalServiceException("Failed to fetch book by id from Google API");
        }
    }
}
