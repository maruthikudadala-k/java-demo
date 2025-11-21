
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
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
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals(book.getTitle(), addedBook.getTitle());
        assertEquals(book.getAuthor(), addedBook.getAuthor());
        assertEquals(book.getPrice(), addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(book.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 40.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Joshua Bloch");

        assertEquals(1, booksByAuthor.size());
        assertEquals(book1.getTitle(), booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldUpdatePriceWhenBookFound() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(book.getId(), 50.0);

        assertTrue(isUpdated);
        assertEquals(50.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean isUpdated = bookService.updatePrice(999L, 50.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 40.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(85.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 40.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksInRange = bookService.getBooksByPriceRange(30.0, 50.0);

        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookWhenIdExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(book.getId());

        assertTrue(isRemoved);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenIdDoesNotExist() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 40.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
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
