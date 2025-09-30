
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Book Title", addedBook.getTitle());
        assertEquals("Author Name", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    void shouldFindBookById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Book Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(99L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldFindBooksByAuthor() {
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        var booksByAuthor = bookService.findByAuthor("Author Name");

        assertFalse(booksByAuthor.isEmpty());
        assertEquals(1, booksByAuthor.size());
        assertEquals("Book Title", booksByAuthor.get(0).getTitle());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(99L, 15.0);
        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValue() {
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    void shouldGetBooksByPriceRange() {
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        var booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertFalse(booksInRange.isEmpty());
        assertEquals(1, booksInRange.size());
        assertEquals("Book Title", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(99L);
        assertFalse(isRemoved);
    }

    @Test
    void shouldGetAllBooks() {
        when(book.getTitle()).thenReturn("Book Title");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        var allBooks = bookService.getAllBooks();

        assertFalse(allBooks.isEmpty());
        assertEquals(1, allBooks.size());
    }

    @Test
    void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        when(book.getTitle()).thenReturn("Book Title");
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        when(book.getTitle()).thenReturn("Book Title");
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
