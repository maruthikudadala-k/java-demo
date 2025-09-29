
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

    @Test
    public void shouldAddBookWhenValidBookProvided() {
        Book book = new Book("Title", "Author", 20.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(20.0, addedBook.getPrice());
        assertNotNull(addedBook.getId());
    }

    @Test
    public void shouldReturnOptionalBookWhenBookExists() {
        Book book = new Book("Title", "Author", 20.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals("Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookDoesNotExist() {
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 20.0);
        Book book2 = new Book("Title2", "Author", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> booksByAuthor = bookService.findByAuthor("Unknown Author");
        
        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 20.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 25.0);
        
        assertTrue(updated);
        assertEquals(25.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean updated = bookService.updatePrice(1L, 25.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 20.0);
        Book book2 = new Book("Title2", "Author", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(45.0, totalValue);
    }

    @Test
    public void shouldReturnEmptyListWhenPriceRangeIsInvalid() {
        Book book1 = new Book("Title1", "Author", 20.0);
        bookService.addBook(book1);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(30.0, 20.0);
        
        assertTrue(booksInRange.isEmpty());
    }

    @Test
    public void shouldReturnBooksByPriceRangeWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 20.0);
        Book book2 = new Book("Title2", "Author", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 30.0);
        
        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Title", "Author", 20.0);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(book.getId());
        
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenBookDoesNotExist() {
        boolean removed = bookService.removeBook(1L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooksWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 20.0);
        Book book2 = new Book("Title2", "Author", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenCollectionIsNotEmpty() {
        Book book = new Book("Title", "Author", 20.0);
        bookService.addBook(book);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCorrectBookCount() {
        Book book1 = new Book("Title1", "Author", 20.0);
        Book book2 = new Book("Title2", "Author", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
