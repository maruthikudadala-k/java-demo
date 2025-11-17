
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookWhenValidBookProvided() {
        Book book = new Book("Title", "Author", 10.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals(book.getTitle(), result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.findByAuthor("Author");

        assertEquals(2, result.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean result = bookService.updatePrice(book.getId(), 20.0);

        assertTrue(result);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean result = bookService.updatePrice(999L, 20.0);

        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenIdExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean result = bookService.removeBook(book.getId());

        assertTrue(result);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenIdDoesNotExist() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        int count = bookService.getBookCount();

        assertEquals(1, count);
    }
}
