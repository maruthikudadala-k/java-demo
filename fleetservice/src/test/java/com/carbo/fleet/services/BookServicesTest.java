
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    void shouldFindBookById() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(book.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(book.getId(), foundBook.get().getId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldFindBooksByAuthor() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorA", 20.0));

        List<Book> booksByAuthorA = bookService.findByAuthor("AuthorA");

        assertEquals(2, booksByAuthorA.size());
    }

    @Test
    void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean updated = bookService.updatePrice(book.getId(), 15.0);

        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistingBook() {
        boolean updated = bookService.updatePrice(999L, 15.0);
        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "AuthorA", 5.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));
        bookService.addBook(new Book("Title3", "AuthorC", 25.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Title2", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean removed = bookService.removeBook(book.getId());

        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean removed = bookService.removeBook(999L);
        assertFalse(removed);
    }

    @Test
    void shouldGetAllBooks() {
        bookService.addBook(new Book("Title1", "AuthorA", 10.0));
        bookService.addBook(new Book("Title2", "AuthorB", 15.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldCheckIfBooksCollectionIsEmpty() {
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
