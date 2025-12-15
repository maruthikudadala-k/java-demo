
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
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> result = bookService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.findByAuthor("Author");

        assertEquals(2, result.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(1L, 20.0);

        assertTrue(updated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingNonExistingBookPrice() {
        boolean updated = bookService.updatePrice(1L, 20.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(2, result.size());
    }

    @Test
    public void shouldRemoveBookById() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean removed = bookService.removeBook(1L);

        assertFalse(removed);
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
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
