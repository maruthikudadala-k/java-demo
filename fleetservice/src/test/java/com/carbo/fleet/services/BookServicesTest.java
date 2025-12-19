
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
        assertEquals("Joshua Bloch", result.getAuthor());
        assertEquals(45.0, result.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Clean Code", "Robert C. Martin", 40.0);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals("Clean Code", result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> result = bookService.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Book One", "Author A", 20.0));
        bookService.addBook(new Book("Book Two", "Author A", 25.0));
        bookService.addBook(new Book("Book Three", "Author B", 30.0));

        List<Book> result = bookService.findByAuthor("Author A");

        assertEquals(2, result.size());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Refactoring", "Martin Fowler", 50.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(book.getId(), 60.0);

        assertTrue(updated);
        assertEquals(60.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean updated = bookService.updatePrice(99L, 60.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book A", "Author A", 10.0));
        bookService.addBook(new Book("Book B", "Author B", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Book 1", "Author 1", 15.0));
        bookService.addBook(new Book("Book 2", "Author 2", 25.0));
        bookService.addBook(new Book("Book 3", "Author 3", 35.0));

        List<Book> result = bookService.getBooksByPriceRange(20.0, 30.0);

        assertEquals(1, result.size());
        assertEquals("Book 2", result.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Book to Remove", "Author", 10.0);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(book.getId());

        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenNotFound() {
        boolean removed = bookService.removeBook(99L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Book", "Author", 10.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("Book", "Author", 10.0));

        assertEquals(1, bookService.getBookCount());
    }
}
