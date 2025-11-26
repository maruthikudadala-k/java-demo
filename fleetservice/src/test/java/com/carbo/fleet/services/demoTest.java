
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
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Sample Title");
        when(book.getAuthor()).thenReturn("Sample Author");
        when(book.getPrice()).thenReturn(10.0);
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        verify(bookService, times(1)).addBook(book);
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        when(book.getAuthor()).thenReturn("Sample Author");
        bookService.addBook(book);

        List<Book> booksByAuthor = bookService.findByAuthor("Sample Author");

        assertFalse(booksByAuthor.isEmpty());
        assertEquals(1, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(1L, 15.0);

        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonexistentBook() {
        boolean updated = bookService.updatePrice(99L, 15.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueCorrectly() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertFalse(booksInRange.isEmpty());
        assertEquals(1, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
        assertFalse(bookService.getAllBooks().contains(book));
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        boolean removed = bookService.removeBook(99L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(1, allBooks.size());
        assertEquals(book, allBooks.get(0));
    }

    @Test
    public void shouldCheckIfBookCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
