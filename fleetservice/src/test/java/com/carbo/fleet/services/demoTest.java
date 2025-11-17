
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    void shouldFindBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        Optional<Book> result = bookService.findById(bookId);

        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindBooksByAuthor() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorA", 20.0));

        List<Book> result = bookService.findByAuthor("AuthorA");

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean result = bookService.updatePrice(bookId, 15.0);

        assertTrue(result);
        assertEquals(15.0, bookService.findById(bookId).get().getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(999L, 15.0);

        assertFalse(result);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 20.0));
        bookService.addBook(new Book("Title3", "AuthorC", 30.0));

        List<Book> result = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getPrice());
    }

    @Test
    void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean result = bookService.removeBook(bookId);

        assertTrue(result);
        assertFalse(bookService.findById(bookId).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    void shouldGetAllBooks() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
