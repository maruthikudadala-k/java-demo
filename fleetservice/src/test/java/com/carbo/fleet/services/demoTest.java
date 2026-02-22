
package com.carbo.fleet.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book result = bookService.addBook(book);
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    public void shouldFindBookByIdSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorSuccessfully() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Another Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> foundBooks = bookService.findByAuthor("Author");
        assertEquals(1, foundBooks.size());
        assertEquals(book1.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 20.0);
        assertTrue(updated);
        assertEquals(20.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> foundBooks = bookService.getBooksByPriceRange(10.0, 20.0);
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
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
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());
        
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        assertEquals(1, bookService.getBookCount());
    }
}
