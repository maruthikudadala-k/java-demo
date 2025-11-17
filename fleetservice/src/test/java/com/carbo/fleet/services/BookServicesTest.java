
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
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
    public void shouldAddBookWhenBookIsValid() {
        when(book.getTitle()).thenReturn("Sample Book");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(9.99);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Sample Book", addedBook.getTitle());
        assertEquals("Author Name", addedBook.getAuthor());
        assertEquals(9.99, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);

        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Book 1", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Book 1", "Author A", 10.0));
        bookService.addBook(new Book("Book 2", "Author B", 15.0));
        bookService.addBook(new Book("Book 3", "Author A", 20.0));

        List<Book> booksByAuthor = bookService.findByAuthor("Author A");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean isUpdated = bookService.updatePrice(1L, 12.0);

        assertTrue(isUpdated);
        assertEquals(12.0, book1.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean isUpdated = bookService.updatePrice(99L, 15.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));
        bookService.addBook(new Book("Book 3", "Author 3", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Book 2", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Book book1 = new Book("Book 1", "Author 1", 10.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenNotExists() {
        boolean isRemoved = bookService.removeBook(99L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book 1", "Author 1", 10.0));
        bookService.addBook(new Book("Book 2", "Author 2", 20.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        bookService.addBook(new Book("Book 1", "Author 1", 10.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());

        bookService.addBook(new Book("Book 1", "Author 1", 10.0));

        assertEquals(1, bookService.getBookCount());
    }
}
