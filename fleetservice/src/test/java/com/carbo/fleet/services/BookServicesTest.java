
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
    public void shouldAddBook() {
        Book book = new Book("Title", "Author", 10.0);

        Book result = bookService.addBook(book);

        assertEquals(book, result);
        assertNotNull(result.getId());
    }

    @Test
    public void shouldFindByIdWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookDoesNotExist() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindByAuthorWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        bookService.addBook(new Book("Title3", "Another Author", 20.0));

        List<Book> result = bookService.findByAuthor("Author");

        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> result = bookService.findByAuthor("Nonexistent Author");

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean result = bookService.updatePrice(1L, 15.0);

        assertTrue(result);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceForNonexistentBook() {
        boolean result = bookService.updatePrice(999L, 15.0);

        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        bookService.addBook(new Book("Title3", "Author", 20.0));

        List<Book> result = bookService.getBooksByPriceRange(10.0, 15.0);

        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        List<Book> result = bookService.getBooksByPriceRange(100.0, 150.0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean result = bookService.removeBook(1L);

        assertTrue(result);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
