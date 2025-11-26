
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

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Sample Book");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(20.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Sample Book", addedBook.getTitle());
        assertEquals("Author Name", addedBook.getAuthor());
        assertEquals(20.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookByIdWhenExists() {
        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));
        Long bookId = bookService.getAllBooks().get(0).getId();

        Optional<Book> foundBook = bookService.findById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals("Sample Book", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookIdNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));
        bookService.addBook(new Book("Another Book", "Author Name", 15.0));
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author Name");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book bookToUpdate = new Book("Sample Book", "Author Name", 20.0);
        bookService.addBook(bookToUpdate);
        Long bookId = bookService.getAllBooks().get(0).getId();

        boolean updated = bookService.updatePrice(bookId, 25.0);

        assertTrue(updated);
        assertEquals(25.0, bookService.findById(bookId).get().getPrice());
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));
        bookService.addBook(new Book("Another Book", "Another Author", 30.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(50.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));
        bookService.addBook(new Book("Another Book", "Another Author", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Sample Book", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book bookToRemove = new Book("Sample Book", "Author Name", 20.0);
        bookService.addBook(bookToRemove);
        Long bookId = bookService.getAllBooks().get(0).getId();

        boolean removed = bookService.removeBook(bookId);

        assertTrue(removed);
        assertFalse(bookService.findById(bookId).isPresent());
    }

    @Test
    public void shouldReturnAllBooksSuccessfully() {
        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));
        bookService.addBook(new Book("Another Book", "Another Author", 30.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfServiceIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("Sample Book", "Author Name", 20.0));

        assertEquals(1, bookService.getBookCount());
    }
}
