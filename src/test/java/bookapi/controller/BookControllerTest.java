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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void testGetAllBooks() throws Exception {
        Mockito.when(bookRepository.findAll()).thenReturn(
            Arrays.asList(new Book(1L, "Book A", "Author A", 2020), new Book(2L, "Book B", "Author B", 2021))
        );

        mockMvc.perform(get("/api/books"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetBookById() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book(1L, "Book A", "Author A", 2020)));

        mockMvc.perform(get("/api/books/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Book A"));
    }

    @Test
    void testCreateBook() throws Exception {
        Book newBook = new Book(null, "New Book", "Author C", 2022);
        Book savedBook = new Book(3L, "New Book", "Author C", 2022);

        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Book\", \"author\":\"Author C\", \"year\":2022}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void testDeleteBook() throws Exception {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/books/1"))
               .andExpect(status().isOk());
    }
}
