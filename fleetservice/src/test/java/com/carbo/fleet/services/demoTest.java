
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
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        when(bookService.generateNewId()).thenReturn(1L);

        Book addedBook = bookService.addBook(book);

        assertEquals(book, addedBook);
        verify(bookService).addBook(book);
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
        Optional<Book> foundBook = bookService.findById(1L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnEmptyListWhenFindingByEmptyAuthor() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);

        List<Book> foundBooks = bookService.findByAuthor("");

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(1L, 15.0);

        assertTrue(updated);
        verify(book).setPrice(15.0);
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(1L, 15.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        when(book.getPrice()).thenReturn(15.0);
        bookService.addBook(book);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        when(book.getPrice()).thenReturn(15.0);
        bookService.addBook(book);

        List<Book> foundBooks = bookService.getBooksByPriceRange(5.0, 12.0);

        assertEquals(1, foundBooks.size());
        assertEquals(10.0, foundBooks.get(0).getPrice());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(1L);

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
    public void shouldReturnTrueWhenBooksIsEmpty() {
        boolean isEmpty = bookService.isEmpty();

        assertTrue(isEmpty);
    }

    @Test
    public void shouldReturnCountOfBooks() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        int count = bookService.getBookCount();

        assertEquals(1, count);
    }
}
