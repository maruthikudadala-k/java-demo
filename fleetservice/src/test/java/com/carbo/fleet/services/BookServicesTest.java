
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

    @Mock
    private BookService.Book bookMock;

    @Test
    public void shouldAddBookSuccessfully() {
        when(bookMock.getTitle()).thenReturn("Effective Java");
        when(bookMock.getAuthor()).thenReturn("Joshua Bloch");
        when(bookMock.getPrice()).thenReturn(45.0);
        
        Book addedBook = bookService.addBook(bookMock);
        
        assertEquals(bookMock, addedBook);
    }

    @Test
    public void shouldFindBookByIdWhenExists() {
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        book.setId(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookIdNotFound() {
        Optional<Book> foundBook = bookService.findById(99L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        Book book2 = new Book("Java Effectively", "Brian Goetz", 30.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> foundBooks = bookService.findByAuthor("Brian Goetz");
        
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Clean Code", "Robert Martin", 40.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(1L, 50.0);
        
        assertTrue(updated);
        assertEquals(50.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenBookIdNotFoundDuringPriceUpdate() {
        boolean updated = bookService.updatePrice(99L, 20.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Design Patterns", "Gamma et al.", 60.0);
        Book book2 = new Book("Refactoring", "Martin Fowler", 45.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(105.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Java Concurrency in Practice", "Brian Goetz", 55.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(40.0, 50.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals(book1, booksInRange.get(0));
    }

    @Test
    public void shouldRemoveBookWhenIdExists() {
        Book book = new Book("Clean Code", "Robert Martin", 40.0);
        book.setId(1L);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingBookIdNotFound() {
        boolean removed = bookService.removeBook(99L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Effective Java", "Joshua Bloch", 45.0);
        Book book2 = new Book("Clean Code", "Robert Martin", 40.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBookCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        Book book = new Book("Effective Java", "Joshua Bloch", 45.0);
        bookService.addBook(book);
        
        assertEquals(1, bookService.getBookCount());
    }
}
