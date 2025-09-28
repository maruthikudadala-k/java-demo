
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private Book book;

    @Test
    void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    void shouldFindBookById() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");

        assertFalse(booksByAuthor.isEmpty());
        assertEquals(1, booksByAuthor.size());
        assertEquals("Test Title", booksByAuthor.get(0).getTitle());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    void shouldCalculateTotalValue() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    void shouldReturnBooksByPriceRange() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertFalse(booksInRange.isEmpty());
        assertEquals(1, booksInRange.size());
        assertEquals("Test Title", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookById() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldReturnAllBooks() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        bookService.addBook(book);
        List<Book> allBooks = bookService.getAllBooks();

        assertFalse(allBooks.isEmpty());
        assertEquals(1, allBooks.size());
        assertEquals("Test Title", allBooks.get(0).getTitle());
    }

    @Test
    void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
