
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

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book bookToFind = new Book("Existing Title", "Existing Author", 15.0);
        bookToFind.setId(1L);
        bookService.addBook(bookToFind);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(bookToFind, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author1", 12.0));
        bookService.addBook(new Book("Title3", "Author2", 15.0));
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author1");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        Book bookToUpdate = new Book("Title", "Author", 10.0);
        bookToUpdate.setId(1L);
        bookService.addBook(bookToUpdate);
        
        boolean updated = bookService.updatePrice(1L, 20.0);
        
        assertTrue(updated);
        assertEquals(20.0, bookToUpdate.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistingBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);
        
        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 5.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));
        bookService.addBook(new Book("Title3", "Author3", 25.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);
        
        assertEquals(1, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book bookToRemove = new Book("Title", "Author", 10.0);
        bookToRemove.setId(1L);
        bookService.addBook(bookToRemove);
        
        boolean removed = bookService.removeBook(1L);
        
        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean removed = bookService.removeBook(999L);
        
        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(1, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        
        assertEquals(1, bookService.getBookCount());
    }
}
