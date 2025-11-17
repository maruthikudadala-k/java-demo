
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
        assertNotNull(addedBook.getId());
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindBookByIdWhenBookExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(book.getId(), foundBook.get().getId());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorWhenBooksExist() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Java Concurrency in Practice", "Brian Goetz", 50.0));
        bookService.addBook(new Book("Clean Code", "Robert C. Martin", 40.0));

        List<Book> booksByAuthor = bookService.findByAuthor("Joshua Bloch");
        assertEquals(1, booksByAuthor.size());
        assertEquals("Effective Java", booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        boolean updated = bookService.updatePrice(book.getId(), 55.0);
        assertTrue(updated);
        assertEquals(55.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 50.0);
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Clean Code", "Robert C. Martin", 40.0));
        double totalValue = bookService.calculateTotalValue();
        assertEquals(85.0, totalValue);
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        List<Book> booksInRange = bookService.getBooksByPriceRange(100.0, 200.0);
        assertTrue(booksInRange.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        boolean removed = bookService.removeBook(book.getId());
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooksWhenBooksExist() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        bookService.addBook(new Book("Clean Code", "Robert C. Martin", 40.0));
        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCountWhenBooksExist() {
        bookService.addBook(new Book("Effective Java", "Joshua Bloch", 45.0));
        int count = bookService.getBookCount();
        assertEquals(1, count);
    }
}
