
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
    public void shouldAddBookWhenValidInput() {
        when(book.getTitle()).thenReturn("Sample Book");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(1L);
        
        Book result = bookService.addBook(book);
        
        assertNotNull(result);
        assertEquals("Sample Book", result.getTitle());
        assertEquals("Author Name", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    public void shouldFindBookByIdWhenExists() {
        Book book1 = new Book("Sample Book", "Author Name", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        Optional<Book> result = bookService.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> result = bookService.findById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorWhenExists() {
        Book book1 = new Book("Sample Book", "Author Name", 10.0);
        bookService.addBook(book1);
        
        List<Book> result = bookService.findByAuthor("Author Name");
        
        assertEquals(1, result.size());
        assertEquals(book1, result.get(0));
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> result = bookService.findByAuthor("Unknown Author");
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book1 = new Book("Sample Book", "Author Name", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        boolean result = bookService.updatePrice(1L, 15.0);
        
        assertTrue(result);
        assertEquals(15.0, book1.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceForNonExistentBook() {
        boolean result = bookService.updatePrice(999L, 15.0);
        
        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeWhenExists() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        List<Book> result = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        
        List<Book> result = bookService.getBooksByPriceRange(15.0, 20.0);
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book1 = new Book("Sample Book", "Author Name", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        boolean result = bookService.removeBook(1L);
        
        assertTrue(result);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);
        
        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooksWhenBooksExist() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 15.0));
        
        List<Book> result = bookService.getAllBooks();
        
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksExist() {
        List<Book> result = bookService.getAllBooks();
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        
        assertEquals(1, bookService.getBookCount());
    }
}
