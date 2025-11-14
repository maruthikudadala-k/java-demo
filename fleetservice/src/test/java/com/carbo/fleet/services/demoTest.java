
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
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookByIdWhenExists() {
        Book book = new Book("Clean Code", "Robert C. Martin", 50.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Clean Code", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookByIdDoesNotExist() {
        Optional<Book> foundBook = bookService.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 40.0));
        
        List<Book> booksByAuthor = bookService.findByAuthor("Joshua Bloch");
        assertEquals(1, booksByAuthor.size());
        assertEquals("Effective Java", booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldUpdatePriceSuccessfullyWhenBookExists() {
        Book book = new Book("Introduction to Algorithms", "Thomas H. Cormen", 60.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 70.0);
        assertTrue(updated);
        assertEquals(70.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistingBook() {
        boolean updated = bookService.updatePrice(999L, 70.0);
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        double totalValue = bookService.calculateTotalValue();
        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Book A", "Author A", 15.0));
        bookService.addBook(new Book("Book B", "Author B", 25.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);
        assertEquals(1, booksInRange.size());
        assertEquals("Book A", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfullyWhenExists() {
        Book book = new Book("Book to Remove", "Author", 30.0);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(book.getId());
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean removed = bookService.removeBook(999L);
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
    public void shouldReturnTrueWhenBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCountOfBooks() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
