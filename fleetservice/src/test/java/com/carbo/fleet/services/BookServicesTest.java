
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
    public void shouldFindBookById() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");
        
        assertFalse(booksByAuthor.isEmpty());
        assertEquals(1, booksByAuthor.size());
        assertEquals("Test Author", booksByAuthor.get(0).getAuthor());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(1L, 15.0);
        
        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 15.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
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
        assertEquals(10.0, booksInRange.get(0).getPrice());
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
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        when(book.getTitle()).thenReturn("Test Title");
        bookService.addBook(book);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertFalse(allBooks.isEmpty());
        assertEquals(1, allBooks.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        when(book.getTitle()).thenReturn("Test Title");
        bookService.addBook(book);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        when(book.getTitle()).thenReturn("Test Title");
        bookService.addBook(book);
        
        assertEquals(1, bookService.getBookCount());
    }
}
