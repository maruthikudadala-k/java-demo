
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book result = bookService.addBook(book);
        
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
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
    public void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean result = bookService.updatePrice(book.getId(), 12.0);
        
        assertTrue(result);
        assertEquals(12.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(999L, 10.0);
        
        assertFalse(result);
    }

    @Test
    public void shouldReturnTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 5.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        List<Book> result = bookService.getBooksByPriceRange(5.0, 10.0);
        
        assertEquals(1, result.size());
        assertEquals(5.0, result.get(0).getPrice());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean result = bookService.removeBook(book.getId());
        
        assertTrue(result);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);
        
        assertFalse(result);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> result = bookService.getAllBooks();
        
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnTrueWhenServiceIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenServiceHasBooks() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
