
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

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Effective Java", result.getTitle());
        assertEquals("Joshua Bloch", result.getAuthor());
        assertEquals(45.0, result.getPrice());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Effective Java", result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.findByAuthor("Joshua Bloch");

        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean result = bookService.updatePrice(1L, 50.0);

        assertTrue(result);
        assertEquals(50.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(999L, 50.0);

        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(95.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getBooksByPriceRange(40.0, 50.0);

        assertEquals(2, result.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean result = bookService.removeBook(1L);

        assertTrue(result);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooksSuccessfully() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());

        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);

        assertEquals(1, bookService.getBookCount());
    }
}
