
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

        assertNotNull(addedBook.getId());
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(addedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(addedBook.getId(), foundBook.get().getId());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorA", 20.0));

        List<Book> booksByAuthor = bookService.findByAuthor("AuthorA");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);
        boolean updated = bookService.updatePrice(addedBook.getId(), 15.0);

        assertTrue(updated);
        assertEquals(15.0, bookService.findById(addedBook.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 15.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 20.0));
        bookService.addBook(new Book("Title3", "AuthorC", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Title2", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);
        boolean removed = bookService.removeBook(addedBook.getId());

        assertTrue(removed);
        assertFalse(bookService.findById(addedBook.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertFalse(removed);
    }

    @Test
    public void shouldGetAllBooks() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 20.0));

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
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
