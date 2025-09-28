
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(1L);

        Book addedBook = bookService.addBook(book);

        assertEquals(book, addedBook);
        assertEquals(1, bookService.getBookCount());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author1", 15.0));
        bookService.addBook(new Book("Title3", "Author2", 20.0));

        List<Book> booksByAuthor = bookService.findByAuthor("Author1");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(1L, 15.0);

        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    void shouldNotUpdatePriceWhenBookNotFound() {
        boolean updated = bookService.updatePrice(99L, 15.0);

        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Book1", "Author1", 10.0));
        bookService.addBook(new Book("Book2", "Author2", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Book1", "Author1", 5.0));
        bookService.addBook(new Book("Book2", "Author2", 15.0));
        bookService.addBook(new Book("Book3", "Author3", 25.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(1, booksInRange.size());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    void shouldNotRemoveBookWhenNotFound() {
        boolean removed = bookService.removeBook(99L);

        assertFalse(removed);
    }

    @Test
    void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenCollectionIsNotEmpty() {
        bookService.addBook(new Book("Test Title", "Test Author", 10.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnCorrectBookCount() {
        bookService.addBook(new Book("Book1", "Author1", 10.0));
        bookService.addBook(new Book("Book2", "Author2", 20.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
