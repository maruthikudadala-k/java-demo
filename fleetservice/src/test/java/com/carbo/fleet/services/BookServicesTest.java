
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.List;
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
        
        assertEquals(book, result);
        assertEquals(1, bookService.getBookCount());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> result = bookService.findById(book.getId());
        
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
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
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
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
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean result = bookService.updatePrice(999L, 20.0);
        
        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));
        
        double result = bookService.calculateTotalValue();
        
        assertEquals(30.0, result);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));
        bookService.addBook(new Book("Title3", "Author", 30.0));
        
        List<Book> result = bookService.getBooksByPriceRange(15.0, 25.0);
        
        assertEquals(1, result.size());
        assertEquals("Title2", result.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean result = bookService.removeBook(book.getId());
        
        assertTrue(result);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    public void shouldNotRemoveBookWhenNotFound() {
        boolean result = bookService.removeBook(999L);
        
        assertFalse(result);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> result = bookService.getAllBooks();
        
        assertEquals(2, result.size());
        assertTrue(result.contains(book1));
        assertTrue(result.contains(book2));
    }

    @Test
    public void shouldReturnTrueWhenBooksAreEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenBooksAreNotEmpty() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));
        
        assertEquals(2, bookService.getBookCount());
    }
}
