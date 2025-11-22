
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book bookMock;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        when(bookMock.getTitle()).thenReturn("Effective Java");
        when(bookMock.getAuthor()).thenReturn("Joshua Bloch");
        when(bookMock.getPrice()).thenReturn(39.99);
        
        Book addedBook = bookService.addBook(bookMock);
        
        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(39.99, addedBook.getPrice());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        when(bookMock.getId()).thenReturn(1L);
        bookService.addBook(bookMock);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(bookMock, foundBook.get());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthorWhenValidAuthorProvided() {
        when(bookMock.getAuthor()).thenReturn("J.K. Rowling");
        bookService.addBook(bookMock);
        
        List<Book> books = bookService.findByAuthor("J.K. Rowling");
        
        assertFalse(books.isEmpty());
        assertEquals(bookMock, books.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> books = bookService.findByAuthor("Unknown Author");
        
        assertTrue(books.isEmpty());
    }

    @Test
    void shouldUpdatePriceWhenValidIdAndPriceProvided() {
        when(bookMock.getId()).thenReturn(1L);
        when(bookMock.getPrice()).thenReturn(29.99);
        bookService.addBook(bookMock);
        
        boolean updated = bookService.updatePrice(1L, 49.99);
        
        assertTrue(updated);
        assertEquals(49.99, bookMock.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 49.99);
        
        assertFalse(updated);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> bookService.updatePrice(1L, -5.0));
    }

    @Test
    void shouldCalculateTotalValueCorrectly() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        Book book2 = new Book("Book 2", "Author 2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        Book book2 = new Book("Book 2", "Author 2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, books.size());
        assertEquals(book1, books.get(0));
    }

    @Test
    void shouldRemoveBookWhenIdExists() {
        when(bookMock.getId()).thenReturn(1L);
        bookService.addBook(bookMock);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingBookWithInvalidId() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    void shouldReturnAllBooks() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        Book book2 = new Book("Book 2", "Author 2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenBooksAreEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        bookService.addBook(book1);
        
        int count = bookService.getBookCount();
        
        assertEquals(1, count);
    }
}
