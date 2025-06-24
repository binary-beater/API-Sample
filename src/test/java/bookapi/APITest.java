package bookapi;

import bookapi.model.Book;
import bookapi.repository.BookRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class APITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldAddBookViaApi() throws Exception {
        String bookJson = """
            {
              "title": "API Test Book",
              "author": "Author",
              "year": 2025
            }
        """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetEmptyBooksList() throws Exception {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void shouldGetBooksListWithData() throws Exception {
        // Add a book directly via repository so GET will return it
        Book book = new Book("Book Title", "Author", 2022);
        bookRepository.save(book);

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Book Title"));
    }
    
    @Test
    void shouldGetBookById() throws Exception {
        Book book = new Book("Book ById", "Author", 2023);
        book = bookRepository.save(book);

        mockMvc.perform(get("/api/books/" + book.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Book ById"));
    }

    @Test
    void shouldReturn404ForNonexistentBook() throws Exception {
        mockMvc.perform(get("/api/books/9999"))
            .andExpect(status().isNotFound());
    }
    @Test
    void shouldUpdateBook() throws Exception {
        Book book = new Book("Old Title", "Author", 2020);
        book = bookRepository.save(book);

        String updateJson = """
            {
            "title": "Updated Title",
            "author": "Author",
            "year": 2021
            }
        """;

        mockMvc.perform(put("/api/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void shouldReturn404OnUpdateNonexistentBook() throws Exception {
        String updateJson = """
            {
            "title": "Updated Title",
            "author": "Author",
            "year": 2021
            }
        """;

        mockMvc.perform(put("/api/books/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldDeleteBookViaApi() throws Exception {
        Book book = new Book("ToDelete", "Author", 2015);
        book = bookRepository.save(book);

        mockMvc.perform(delete("/api/books/" + book.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404OnDeleteNonexistentBook() throws Exception {
        mockMvc.perform(delete("/api/books/9999"))
            .andExpect(status().isNotFound());
    }
}
