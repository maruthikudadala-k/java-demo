
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Effective Java");
        when(book.getAuthor()).thenReturn("Joshua Bloch");
        when(book.getPrice()).thenReturn(45.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Effective Java", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 50.0));
        
        List<Book> books = bookService.findByAuthor("Joshua Bloch");
        
        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> books = bookService.findByAuthor("Unknown Author");
        
        assertTrue(books.isEmpty());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(1L, 50.0);
        
        assertTrue(isUpdated);
        assertEquals(50.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistingBook() {
        boolean isUpdated = bookService.updatePrice(999L, 50.0);
        
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 50.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(95.0, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        bookService.addBook(new Book("Book 1", "Author 1", 30.0));
        bookService.addBook(new Book("Book 2", "Author 2", 50.0));
        
        List<Book> books = bookService.getBooksByPriceRange(20.0, 40.0);
        
        assertEquals(1, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean isRemoved = bookService.removeBook(1L);
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean isRemoved = bookService.removeBook(999L);
        
        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 50.0));
        
        List<Book> books = bookService.getAllBooks();
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenCollectionIsNotEmpty() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCorrectBookCount() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 50.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
