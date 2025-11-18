
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
        Book book = new Book("Title", "Author", 10.0);

        Book addedBook = bookService.addBook(book);

        assertEquals(book, addedBook);
        assertEquals(1, bookService.getBookCount());
    }

    @Test
    public void shouldReturnBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(book.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(1L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> foundBooks = bookService.findByAuthor("Author1");

        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(book.getId(), 20.0);

        assertTrue(updated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean updated = bookService.updatePrice(1L, 20.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        bookService.addBook(new Book("Title3", "Author3", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Title2", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(book.getId());

        assertTrue(removed);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    public void shouldNotRemoveBookWhenNotFound() {
        boolean removed = bookService.removeBook(1L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
