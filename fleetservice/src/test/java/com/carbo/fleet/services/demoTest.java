
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
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
    void shouldAddBookWhenValidBookProvided() {
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
    void shouldReturnBookWhenFoundById() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Test Title 1", "Test Author", 10.0);
        Book book2 = new Book("Test Title 2", "Test Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(1L, 20.0);
        
        assertTrue(isUpdated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    void shouldNotUpdatePriceWhenBookNotFound() {
        boolean isUpdated = bookService.updatePrice(999L, 20.0);
        
        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 15.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Book 1", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean isRemoved = bookService.removeBook(1L);
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldNotRemoveBookWhenNotFound() {
        boolean isRemoved = bookService.removeBook(999L);
        
        assertFalse(isRemoved);
    }

    @Test
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenServiceIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenServiceHasBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
