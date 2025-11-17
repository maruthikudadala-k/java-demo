
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
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        Optional<Book> foundBook = bookService.findById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals(bookId, foundBook.get().getId());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
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
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean updated = bookService.updatePrice(bookId, 50.0);

        assertTrue(updated);
        assertEquals(50.0, bookService.findById(bookId).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 50.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 40.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(85.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 40.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(30.0, 50.0);

        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean removed = bookService.removeBook(bookId);

        assertTrue(removed);
        assertFalse(bookService.findById(bookId).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 40.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));

        assertEquals(1, bookService.getBookCount());
    }
}
