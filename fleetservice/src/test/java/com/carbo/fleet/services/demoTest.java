
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookWhenValid() {
        Book book = new Book("Title", "Author", 10.0);
        
        Book result = bookService.addBook(book);
        
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    public void shouldFindByIdWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals(book.getId(), foundBook.get().getId());
    }

    @Test
    public void shouldReturnEmptyWhenFindByIdAndBookDoesNotExist() {
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> foundBooks = bookService.findByAuthor("Author");
        
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 15.0);
        
        assertTrue(updated);
        assertEquals(15.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatePriceAndBookDoesNotExist() {
        boolean updated = bookService.updatePrice(1L, 15.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnEmptyListWhenGettingBooksByPriceRange() {
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);
        
        assertTrue(booksInRange.isEmpty());
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
    public void shouldReturnFalseWhenRemoveBookAndBookDoesNotExist() {
        boolean removed = bookService.removeBook(1L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooksWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenIsEmptyAndNoBooks() {
        boolean isEmpty = bookService.isEmpty();
        
        assertTrue(isEmpty);
    }

    @Test
    public void shouldReturnFalseWhenIsEmptyAndBooksExist() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        boolean isEmpty = bookService.isEmpty();
        
        assertFalse(isEmpty);
    }

    @Test
    public void shouldReturnBookCountWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
