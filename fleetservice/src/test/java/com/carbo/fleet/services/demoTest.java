
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
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindBookByIdSuccessfully() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorSuccessfully() {
        Book book1 = new Book("Test Title 1", "Test Author", 10.0);
        Book book2 = new Book("Test Title 2", "Another Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");
        
        assertEquals(1, booksByAuthor.size());
        assertEquals("Test Title 1", booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 15.0);
        
        assertTrue(updated);
        assertEquals(15.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 15.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        Book book1 = new Book("Test Title 1", "Test Author", 10.0);
        Book book2 = new Book("Test Title 2", "Another Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        Book book1 = new Book("Test Title 1", "Test Author", 10.0);
        Book book2 = new Book("Test Title 2", "Another Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 12.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Test Title 1", booksInRange.get(0).getTitle());
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
    public void shouldGetAllBooksSuccessfully() {
        Book book1 = new Book("Test Title 1", "Test Author", 10.0);
        Book book2 = new Book("Test Title 2", "Another Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());
        
        Book book = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        
        assertEquals(1, bookService.getBookCount());
    }
}
