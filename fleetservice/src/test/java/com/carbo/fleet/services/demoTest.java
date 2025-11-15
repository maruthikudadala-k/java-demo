
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

    @Test
    public void shouldAddBookWhenBookIsValid() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindByIdWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals("Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindByAuthorWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author1", 15.0));
        
        List<Book> books = bookService.findByAuthor("Author1");
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> books = bookService.findByAuthor("Unknown Author");
        
        assertTrue(books.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 20.0);
        
        assertTrue(updated);
        assertEquals(20.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean updated = bookService.updatePrice(999L, 20.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        List<Book> books = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, books.size());
        assertEquals("Title1", books.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(book.getId());
        
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenBookNotFound() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldGetAllBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        List<Book> books = bookService.getAllBooks();
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldCheckIfEmptyWhenNoBooks() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldCheckBookCount() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(1, count);
    }
}
