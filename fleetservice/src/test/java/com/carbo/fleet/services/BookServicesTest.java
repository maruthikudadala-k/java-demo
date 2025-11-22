
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
    public void shouldAddBookWhenValidBookProvided() {
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
    public void shouldFindBookByIdWhenBookExists() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Book One", "Author A", 15.0);
        Book book2 = new Book("Book Two", "Author A", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author A");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(1L, 15.0);
        
        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceForNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(1L, 15.0);
        
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author B", 20.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author B", 20.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Book One", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        boolean isRemoved = bookService.removeBook(1L);
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(1L);
        
        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Book One", "Author A", 10.0);
        Book book2 = new Book("Book Two", "Author B", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        bookService.addBook(new Book("Test Title", "Test Author", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        bookService.addBook(new Book("Test Title", "Test Author", 10.0));
        
        assertEquals(1, bookService.getBookCount());
    }
}
