package bookapi.repository;

import bookapi.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("bookapi.model")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveBook() {
        Book book = new Book(null, "Test Book", "Test Author", 2023);
        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
        assertEquals("Test Book", saved.getTitle());
    }

    @Test
    void testFindBookById() {
        Book book = new Book(null, "Lookup Book", "Author L", 2022);
        Book saved = bookRepository.save(book);

        Optional<Book> found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Lookup Book", found.get().getTitle());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(null, "Delete Me", "Author D", 2021);
        Book saved = bookRepository.save(book);
        bookRepository.deleteById(saved.getId());

        assertFalse(bookRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void testFindAllBooks() {
        bookRepository.save(new Book(null, "Book 1", "A", 2019));
        bookRepository.save(new Book(null, "Book 2", "B", 2020));

        List<Book> all = bookRepository.findAll();
        assertEquals(2, all.size());
    }
}
