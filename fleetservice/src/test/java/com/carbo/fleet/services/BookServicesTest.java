
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

    @Test
    public void shouldAddBookSuccessfully() {
        // Arrange
        Book book = new Book("Title", "Author", 10.0);
        
        // Act
        Book addedBook = bookService.addBook(book);
        
        // Assert
        assertNotNull(addedBook);
        assertEquals(book.getTitle(), addedBook.getTitle());
        assertEquals(book.getAuthor(), addedBook.getAuthor());
        assertEquals(book.getPrice(), addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        // Arrange
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        // Act
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        // Act
        Optional<Book> foundBook = bookService.findById(999L);
        
        // Assert
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnEmptyListWhenFindingByEmptyAuthor() {
        // Act
        var books = bookService.findByAuthor("");
        
        // Assert
        assertTrue(books.isEmpty());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        // Arrange
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        // Act
        boolean updated = bookService.updatePrice(book.getId(), 15.0);
        
        // Assert
        assertTrue(updated);
        assertEquals(15.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldCalculateTotalValueCorrectly() {
        // Arrange
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        // Act
        double totalValue = bookService.calculateTotalValue();
        
        // Assert
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        // Arrange
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        // Act
        var books = bookService.getBooksByPriceRange(5.0, 15.0);
        
        // Assert
        assertEquals(1, books.size());
        assertEquals("Title1", books.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        // Arrange
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        // Act
        boolean removed = bookService.removeBook(book.getId());
        
        // Assert
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnAllBooks() {
        // Arrange
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        // Act
        var books = bookService.getAllBooks();
        
        // Assert
        assertEquals(2, books.size());
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        // Act
        boolean isEmpty = bookService.isEmpty();
        
        // Assert
        assertTrue(isEmpty);
    }

    @Test
    public void shouldReturnBookCount() {
        // Arrange
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        // Act
        int count = bookService.getBookCount();
        
        // Assert
        assertEquals(1, count);
    }
}
