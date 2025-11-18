
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
        Book book = new Book("Test Title", "Test Author", 10.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals(book.getTitle(), addedBook.getTitle());
        assertEquals(book.getAuthor(), addedBook.getAuthor());
        assertEquals(book.getPrice(), addedBook.getPrice());
    }

    @Test
    public void shouldFindBookById() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
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
        Book book1 = new Book("Title 1", "Author A", 10.0);
        Book book2 = new Book("Title 2", "Author A", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.findByAuthor("Author A");
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 12.0);
        
        assertTrue(updated);
        assertEquals(12.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 12.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Title 2", "Author B", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Title 2", "Author B", 20.0));
        
        List<Book> books = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, books.size());
        assertEquals("Title 1", books.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(book.getId());
        
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldGetAllBooks() {
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Title 2", "Author B", 15.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        
        assertEquals(1, bookService.getBookCount());
    }
}
