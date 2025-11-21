
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
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book result = bookService.addBook(book);
        
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
    }

    @Test
    public void shouldReturnBookById() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.findByAuthor("Author");
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(1L, 20.0);
        
        assertTrue(updated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        
        List<Book> books = bookService.getBooksByPriceRange(10.0, 15.0);
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        List<Book> books = bookService.getAllBooks();
        
        assertEquals(1, books.size());
        assertEquals(book.getTitle(), books.get(0).getTitle());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
