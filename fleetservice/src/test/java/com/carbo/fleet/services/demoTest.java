
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
    public void shouldAddBookWhenValid() {
        Book book = new Book("Title", "Author", 10.0);
        Book result = bookService.addBook(book);
        assertEquals(book, result);
    }

    @Test
    public void shouldFindBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Optional<Book> result = bookService.findById(book.getId());
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> result = bookService.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorWhenExists() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> result = bookService.findByAuthor("Author");
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> result = bookService.findByAuthor("Nonexistent Author");
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean result = bookService.updatePrice(book.getId(), 15.0);
        assertTrue(result);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceForNonexistentBook() {
        boolean result = bookService.updatePrice(1L, 15.0);
        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        double result = bookService.calculateTotalValue();
        assertEquals(25.0, result);
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        List<Book> result = bookService.getBooksByPriceRange(15.0, 20.0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean result = bookService.removeBook(book.getId());
        assertTrue(result);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        boolean result = bookService.removeBook(1L);
        assertFalse(result);
    }

    @Test
    public void shouldReturnAllBooksWhenRequested() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnTrueWhenBooksAreEmpty() {
        boolean result = bookService.isEmpty();
        assertTrue(result);
    }

    @Test
    public void shouldReturnBookCountWhenBooksAdded() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        int result = bookService.getBookCount();
        assertEquals(1, result);
    }
}
