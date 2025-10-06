
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        // Arrange
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        
        // Act
        Book result = bookService.addBook(book);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        // Arrange
        Book bookToAdd = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToAdd);
        
        // Act
        Optional<Book> result = bookService.findById(bookToAdd.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(bookToAdd.getTitle(), result.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        // Act
        Optional<Book> result = bookService.findById(999L);
        
        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        // Arrange
        bookService.addBook(new Book("Test Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Test Title 2", "Author B", 15.0));
        bookService.addBook(new Book("Test Title 3", "Author A", 20.0));
        
        // Act
        List<Book> result = bookService.findByAuthor("Author A");
        
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdatePriceWhenBookFound() {
        // Arrange
        Book bookToAdd = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToAdd);
        
        // Act
        boolean result = bookService.updatePrice(bookToAdd.getId(), 15.0);
        
        // Assert
        assertTrue(result);
        assertEquals(15.0, bookService.findById(bookToAdd.getId()).get().getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        // Act
        boolean result = bookService.updatePrice(999L, 15.0);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void shouldCalculateTotalValue() {
        // Arrange
        bookService.addBook(new Book("Test Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Test Title 2", "Author B", 20.0));
        
        // Act
        double result = bookService.calculateTotalValue();
        
        // Assert
        assertEquals(30.0, result);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        // Arrange
        bookService.addBook(new Book("Test Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Test Title 2", "Author B", 20.0));
        
        // Act
        List<Book> result = bookService.getBooksByPriceRange(5.0, 15.0);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Title 1", result.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookWhenFound() {
        // Arrange
        Book bookToAdd = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToAdd);
        
        // Act
        boolean result = bookService.removeBook(bookToAdd.getId());
        
        // Assert
        assertTrue(result);
        assertFalse(bookService.findById(bookToAdd.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        // Act
        boolean result = bookService.removeBook(999L);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnAllBooks() {
        // Arrange
        bookService.addBook(new Book("Test Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Test Title 2", "Author B", 20.0));
        
        // Act
        List<Book> result = bookService.getAllBooks();
        
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnTrueWhenServiceIsEmpty() {
        // Act
        boolean result = bookService.isEmpty();
        
        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenBooksArePresent() {
        // Arrange
        bookService.addBook(new Book("Test Title", "Test Author", 10.0));
        
        // Act
        boolean result = bookService.isEmpty();
        
        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnBookCount() {
        // Arrange
        bookService.addBook(new Book("Test Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Test Title 2", "Author B", 20.0));
        
        // Act
        int count = bookService.getBookCount();
        
        // Assert
        assertEquals(2, count);
    }
}
