package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(404).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<?> handleExternalService(ExternalServiceException ex) {
        return ResponseEntity.status(502).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(500).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Internal Server Error"
                )
        );
    }
}
