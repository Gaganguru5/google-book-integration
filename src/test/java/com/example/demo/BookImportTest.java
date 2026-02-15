package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.demo.db.BookRepository;
import com.example.demo.google.GoogleBook; 
import com.example.demo.google.GoogleBookService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookImportTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @MockitoBean
    private GoogleBookService googleBookService;

    @Test
    void testImportBook_Success() throws Exception {
        String googleId = "test_vol_1";

        GoogleBook.VolumeInfo volumeInfo = new GoogleBook.VolumeInfo(
                "Test Title", List.of("Test Author"), "2023", "Publisher", 
                100, "BOOK", "NOT_MATURE", null, "en", "link", "link"
        );
        
        GoogleBook.Item mockItem = new GoogleBook.Item(
                googleId, "selfLink", volumeInfo, null
        );

        Mockito.when(googleBookService.getBookById(googleId)).thenReturn(mockItem);

        mockMvc.perform(post("/books/{googleId}", googleId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(googleId))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"));
        
        assert(bookRepository.existsById(googleId));
    }
}