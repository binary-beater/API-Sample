package bookapi.controller;

import bookapi.model.Book;
import bookapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnListOfBooks() throws Exception {
        Book book = new Book();
        book.setTitle("Mock Book");

        Mockito.when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Mock Book"));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooks() throws Exception {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    void shouldReturnBookById() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("TestTitle");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle"));
    }

    @Test
    void shouldReturn404ForMissingBookById() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBook() throws Exception {
        Book book = new Book("New", "Author", 2020);
        book.setId(1L);

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        String bookJson = """
            {
            "title": "New",
            "author": "Author",
            "year": 2020
            }
        """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        Book existing = new Book("Old", "Author", 2010);
        existing.setId(1L);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(existing);

        String updateJson = """
            {
            "title": "Updated",
            "author": "Author",
            "year": 2021
            }
        """;

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldReturn404OnUpdateMissingBook() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        String updateJson = """
            {
            "title": "Updated",
            "author": "Author",
            "year": 2021
            }
        """;

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        Book book = new Book("ToDelete", "Author", 2010);
        book.setId(1L);

        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(bookRepository).deleteById(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404OnDeleteMissingBook() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());
    }
}