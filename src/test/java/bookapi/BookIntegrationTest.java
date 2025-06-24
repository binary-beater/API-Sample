package bookapi;

import bookapi.model.Book;
import bookapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveAndFetchBook() {
        Book book = new Book();
        book.setTitle("Integration Test Book");
        book.setAuthor("Test Author");
        book.setYear(2025);

        Book saved = bookRepository.save(book);
        Book found = bookRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Integration Test Book", found.getTitle());
    }
    @Test
    void shouldUpdateBook() {
        Book book = new Book("Original Title", "Original Author", 2020);
        Book saved = bookRepository.save(book);

        saved.setTitle("Updated Title");
        Book updated = bookRepository.save(saved);

        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void shouldDeleteBook() {
        Book book = new Book("Delete Title", "Del Author", 2018);
        Book saved = bookRepository.save(book);
        Long id = saved.getId();

        bookRepository.deleteById(id);
        assertFalse(bookRepository.findById(id).isPresent());
    }

}
