
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoJUnitRunner.class)
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
        // Arrange
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        // Act
        Optional<Book> foundBook = bookService.findById(1L);

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        // Arrange
        bookService.addBook(new Book("Title 1", "Author 1", 10.0));
        bookService.addBook(new Book("Title 2", "Author 1", 15.0));
        
        // Act
        List<Book> foundBooks = bookService.findByAuthor("Author 1");

        // Assert
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        // Arrange
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        // Act
        boolean isUpdated = bookService.updatePrice(1L, 12.0);

        // Assert
        assertTrue(isUpdated);
        assertEquals(12.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldCalculateTotalValue() {
        // Arrange
        bookService.addBook(new Book("Title 1", "Author 1", 10.0));
        bookService.addBook(new Book("Title 2", "Author 2", 15.0));
        
        // Act
        double totalValue = bookService.calculateTotalValue();

        // Assert
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        // Arrange
        bookService.addBook(new Book("Title 1", "Author 1", 10.0));
        bookService.addBook(new Book("Title 2", "Author 2", 15.0));
        
        // Act
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 15.0);

        // Assert
        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        // Arrange
        Book book = new Book("Test Title", "Test Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        // Act
        boolean isRemoved = bookService.removeBook(1L);

        // Assert
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnAllBooks() {
        // Arrange
        bookService.addBook(new Book("Title 1", "Author 1", 10.0));
        bookService.addBook(new Book("Title 2", "Author 2", 15.0));

        // Act
        List<Book> allBooks = bookService.getAllBooks();

        // Assert
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBookCollectionIsEmpty() {
        // Act
        boolean isEmpty = bookService.isEmpty();

        // Assert
        assertTrue(isEmpty);
    }

    @Test
    public void shouldGetBookCount() {
        // Arrange
        bookService.addBook(new Book("Title 1", "Author 1", 10.0));
        
        // Act
        int count = bookService.getBookCount();

        // Assert
        assertEquals(1, count);
    }
}
