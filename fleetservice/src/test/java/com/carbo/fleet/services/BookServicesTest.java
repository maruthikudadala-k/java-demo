
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

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Sample Title");
        when(book.getAuthor()).thenReturn("Sample Author");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(null);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Sample Title", addedBook.getTitle());
        assertEquals("Sample Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
        verify(book).setId(anyLong());
        verify(bookService).addBook(book);
    }

    @Test
    public void shouldFindBookById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Sample Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        when(book.getAuthor()).thenReturn("Author A");
        bookService.addBook(book);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author A");

        assertEquals(1, booksByAuthor.size());
        assertEquals("Author A", booksByAuthor.get(0).getAuthor());
    }

    @Test
    public void shouldUpdateBookPrice() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(1L, 15.0);
        
        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
        verify(book).setPrice(15.0);
    }

    @Test
    public void shouldCalculateTotalValue() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, booksInRange.size());
        assertEquals(10.0, booksInRange.get(0).getPrice());
    }

    @Test
    public void shouldRemoveBookById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    public void shouldReturnAllBooks() {
        when(book.getTitle()).thenReturn("Sample Title");
        when(book.getAuthor()).thenReturn("Sample Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(1, allBooks.size());
        assertEquals("Sample Title", allBooks.get(0).getTitle());
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
