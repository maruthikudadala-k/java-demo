
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
        assertEquals(book.getTitle(), addedBook.getTitle());
        assertEquals(book.getAuthor(), addedBook.getAuthor());
        assertEquals(book.getPrice(), addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookById() {
        Book book = new Book("Clean Code", "Robert C. Martin", 40.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        Optional<Book> foundBook = bookService.findById(bookId);

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
        Book book1 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        Book book2 = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Joshua Bloch");

        assertEquals(1, booksByAuthor.size());
        assertEquals(book2.getTitle(), booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Design Patterns", "Erich Gamma", 60.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean updated = bookService.updatePrice(bookId, 70.0);

        assertTrue(updated);
        assertEquals(70.0, bookService.findById(bookId).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonexistentBook() {
        boolean updated = bookService.updatePrice(999L, 70.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Book A", "Author A", 20.0);
        Book book2 = new Book("Book B", "Author B", 30.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(50.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Book A", "Author A", 10.0);
        Book book2 = new Book("Book B", "Author B", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Book to Remove", "Author", 15.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean removed = bookService.removeBook(bookId);

        assertTrue(removed);
        assertFalse(bookService.findById(bookId).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        Book book2 = new Book("Book 2", "Author 2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Non-empty Book", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Some Book", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
