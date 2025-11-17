
package com.carbo.fleet.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals("Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldFindBooksByAuthorSuccessfully() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> books = bookService.findByAuthor("Author");
        
        assertEquals(2, books.size());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean updated = bookService.updatePrice(book.getId(), 12.0);
        
        assertTrue(updated);
        assertEquals(12.0, book.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 12.0);
        
        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValueSuccessfully() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRangeSuccessfully() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 20.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 15.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Title1", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookByIdSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean removed = bookService.removeBook(book.getId());
        
        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    void shouldReturnAllBooksSuccessfully() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenCollectionIsNotEmpty() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnCorrectBookCount() {
        bookService.addBook(new Book("Title1", "Author", 10.0));
        bookService.addBook(new Book("Title2", "Author", 15.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
