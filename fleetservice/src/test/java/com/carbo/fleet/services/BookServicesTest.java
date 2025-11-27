
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

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

        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
        assertEquals(1L, addedBook.getId());
    }

    @Test
    public void shouldFindBookByIdSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorSuccessfully() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);

        var foundBooks = bookService.findByAuthor("Test Author");

        assertEquals(1, foundBooks.size());
        assertEquals("Test Author", foundBooks.get(0).getAuthor());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        var foundBooks = bookService.findByAuthor("Unknown Author");

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistingBook() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        when(book.getPrice()).thenReturn(20.0);
        Book anotherBook = new Book("Second Book", "Another Author", 20.0);
        bookService.addBook(anotherBook);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRangeSuccessfully() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        Book anotherBook = new Book("Second Book", "Another Author", 20.0);
        bookService.addBook(anotherBook);

        var foundBooks = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, foundBooks.size());
        assertEquals("Test Title", foundBooks.get(0).getTitle());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        var foundBooks = bookService.getBooksByPriceRange(30.0, 40.0);

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooksSuccessfully() {
        when(book.getTitle()).thenReturn("Test Title");
        bookService.addBook(book);
        Book anotherBook = new Book("Second Book", "Another Author", 20.0);
        bookService.addBook(anotherBook);

        var allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmptySuccessfully() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
