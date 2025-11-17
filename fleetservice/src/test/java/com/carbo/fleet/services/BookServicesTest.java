
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;
import java.util.List;

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
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Clean Code", "Robert C. Martin", 40.0);
        bookService.addBook(book);

        Optional<Book> result = bookService.findById(book.getId());

        assertTrue(result.isPresent());
        assertEquals(book.getTitle(), result.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> result = bookService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Title 1", "Author A", 30.0);
        Book book2 = new Book("Title 2", "Author A", 35.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> result = bookService.findByAuthor("Author A");

        assertEquals(2, result.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 20.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(book.getId(), 25.0);

        assertTrue(updated);
        assertEquals(25.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceForNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 25.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Book 1", "Author", 10.0));
        bookService.addBook(new Book("Book 2", "Author", 20.0));
        bookService.addBook(new Book("Book 3", "Author", 30.0));

        List<Book> result = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, result.size());
        assertEquals("Book 2", result.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Book to Remove", "Author", 15.0);
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
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book 1", "Author", 10.0));
        bookService.addBook(new Book("Book 2", "Author", 20.0));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldCheckIfBookCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Not Empty", "Author", 10.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("New Book", "Author", 10.0));

        assertEquals(1, bookService.getBookCount());
    }
}
