
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

        Book result = bookService.addBook(book);

        assertNotNull(result);
        verify(book, times(1)).setId(anyLong());
        verify(bookService, times(1)).addBook(book);
    }

    @Test
    public void shouldFindBookById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(2L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);
        
        List<Book> foundBooks = bookService.findByAuthor("Test Author");

        assertEquals(1, foundBooks.size());
        assertEquals("Test Author", foundBooks.get(0).getAuthor());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> foundBooks = bookService.findByAuthor("Unknown Author");
        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean result = bookService.updatePrice(1L, 15.0);

        assertTrue(result);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(2L, 15.0);
        assertFalse(result);
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
        
        List<Book> foundBooks = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, foundBooks.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        when(book.getPrice()).thenReturn(20.0);
        bookService.addBook(book);
        
        List<Book> foundBooks = bookService.getBooksByPriceRange(5.0, 15.0);

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        boolean result = bookService.removeBook(1L);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(2L);
        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooks() {
        when(book.getTitle()).thenReturn("Test Title");
        bookService.addBook(book);
        
        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(1, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        int count = bookService.getBookCount();

        assertEquals(1, count);
    }
}
