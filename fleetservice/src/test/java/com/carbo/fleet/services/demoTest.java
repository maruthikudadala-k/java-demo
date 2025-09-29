
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
    public void shouldAddBookWhenValidBook() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.99);

        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.99, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.99);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(99L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthorWhenAuthorExists() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.99);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 39.99);
        bookService.addBook(book1);
        bookService.addBook(book2);

        var booksByAuthor = bookService.findByAuthor("Joshua Bloch");
        
        assertEquals(1, booksByAuthor.size());
        assertEquals("Effective Java", booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorDoesNotExist() {
        var booksByAuthor = bookService.findByAuthor("Nonexistent Author");
        
        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.99);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(book.getId(), 50.00);
        
        assertTrue(updated);
        assertEquals(50.00, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean updated = bookService.updatePrice(99L, 50.00);
        
        assertFalse(updated);
    }

    @Test
    public void shouldReturnTotalValueOfBooks() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.99);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 39.99);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(85.98, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.99);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 39.99);
        bookService.addBook(book1);
        bookService.addBook(book2);

        var booksInRange = bookService.getBooksByPriceRange(40.00, 50.00);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Effective Java", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.99);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(book.getId());
        
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenDoesNotExist() {
        boolean removed = bookService.removeBook(99L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldGetAllBooks() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.99);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 39.99);
        bookService.addBook(book1);
        bookService.addBook(book2);

        var allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.99));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.99));
        
        assertEquals(1, bookService.getBookCount());
    }
}
