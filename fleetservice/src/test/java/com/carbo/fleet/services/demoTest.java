
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
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals(book.getTitle(), result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> result = bookService.findById(1L);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
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
        
        boolean result = bookService.updatePrice(book.getId(), 20.0);

        assertTrue(result);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(1L, 20.0);
        
        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> result = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, result.size());
        assertEquals(book1.getTitle(), result.get(0).getTitle());
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
        boolean result = bookService.removeBook(1L);
        
        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooks() {
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
