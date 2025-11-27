
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
    public void shouldAddBook() {
        Book book = new Book("Title", "Author", 10.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    public void shouldReturnBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals(book.getTitle(), result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Another Author", 15.0));
        
        List<Book> result = bookService.findByAuthor("Author");

        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(book.getId(), 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));
        
        List<Book> result = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(book.getId());

        assertTrue(isRemoved);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenNotFound() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldGetAllBooks() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldCheckIfEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
