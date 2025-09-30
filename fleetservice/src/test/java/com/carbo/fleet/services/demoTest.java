
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
    public void shouldAddBookWhenValid() {
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
    public void shouldFindByIdWhenExists() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyWhenFindByIdDoesNotExist() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindByAuthorWhenExists() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        List<Book> foundBooks = bookService.findByAuthor("Test Author");
        
        assertFalse(foundBooks.isEmpty());
        assertEquals(1, foundBooks.size());
        assertEquals(book, foundBooks.get(0));
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(1L, 15.0);
        
        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean updated = bookService.updatePrice(999L, 15.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertFalse(booksInRange.isEmpty());
        assertEquals(1, booksInRange.size());
        assertEquals(book, booksInRange.get(0));
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenDoesNotExist() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldGetAllBooks() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertFalse(allBooks.isEmpty());
        assertEquals(1, allBooks.size());
        assertEquals(book, allBooks.get(0));
    }

    @Test
    public void shouldCheckIfEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
