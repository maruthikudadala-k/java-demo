
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

    @Test
    void shouldAddBookSuccessfullyWhenValidBookIsGiven() {
        Book book = new Book("Title", "Author", 10.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNotFoundById() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnListOfBooksByAuthor() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorA", 20.0));

        List<Book> result = bookService.findByAuthor("AuthorA");

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdatePriceSuccessfullyWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean result = bookService.updatePrice(book.getId(), 15.0);

        assertTrue(result);
        assertEquals(15.0, book.getPrice());
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
    void shouldReturnListOfBooksInPriceRange() {
        bookService.addBook(new Book("Title1", "AuthorA", 5.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorC", 25.0));

        List<Book> result = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(1, result.size());
        assertEquals("Title2", result.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookSuccessfullyWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean result = bookService.removeBook(book.getId());

        assertTrue(result);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnTrueWhenCollectionIsEmpty() {
        boolean result = bookService.isEmpty();

        assertTrue(result);
    }

    @Test
    void shouldReturnBookCount() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));

        int count = bookService.getBookCount();

        assertEquals(1, count);
    }
}
