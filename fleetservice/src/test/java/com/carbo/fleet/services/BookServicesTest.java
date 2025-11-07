
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
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
    public void shouldAddBookWhenValidBookIsProvided() {
        when(book.getTitle()).thenReturn("Sample Title");
        when(book.getAuthor()).thenReturn("Sample Author");
        when(book.getPrice()).thenReturn(10.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Sample Title", addedBook.getTitle());
        assertEquals("Sample Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenIdExists() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Title1", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Book> foundBook = bookService.findById(99L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthorWhenExists() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        Book book2 = new Book("Title2", "Author1", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.findByAuthor("Author1");
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> books = bookService.findByAuthor("Unknown Author");
        
        assertTrue(books.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookIdExists() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        boolean updated = bookService.updatePrice(1L, 20.0);
        
        assertTrue(updated);
        assertEquals(20.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceForNonExistentBook() {
        boolean updated = bookService.updatePrice(99L, 20.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, books.size());
        assertEquals("Title1", books.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenIdExists() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(99L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.getAllBooks();
        
        assertEquals(2, books.size());
    }

    @Test
    public void shouldReturnTrueWhenNoBooksExist() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenBooksExist() {
        Book book1 = new Book("Title1", "Author1", 15.0);
        bookService.addBook(book1);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCountOfBooks() {
        assertEquals(0, bookService.getBookCount());
        Book book1 = new Book("Title1", "Author1", 15.0);
        bookService.addBook(book1);
        
        assertEquals(1, bookService.getBookCount());
    }
}
