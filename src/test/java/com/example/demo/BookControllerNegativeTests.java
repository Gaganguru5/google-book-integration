package com.example.demo;

import com.example.demo.google.GoogleBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerNegativeTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoogleBookService googleBookService;

    @Test
    void testGetAllBooks_NoData() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testImportBook_GoogleReturnsNull() throws Exception {
        when(googleBookService.getBookById("invalid"))
                .thenReturn(null);

        mockMvc.perform(post("/books/{id}", "invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testImportBook_ExternalFailure() throws Exception {
        when(googleBookService.getBookById("error"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/books/{id}", "error"))
                .andExpect(status().isInternalServerError());
    }
}