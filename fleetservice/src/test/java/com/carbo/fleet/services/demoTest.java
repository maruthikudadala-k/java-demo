
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
    private BookService.Book book;

    @Test
    public void shouldAddBookWhenValidBook() {
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
    public void shouldFindByIdWhenBookExists() {
        Book bookToAdd = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToAdd);
        
        Optional<Book> foundBook = bookService.findById(bookToAdd.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals(bookToAdd.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author1");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book bookToUpdate = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToUpdate);
        
        boolean isUpdated = bookService.updatePrice(bookToUpdate.getId(), 20.0);
        
        assertTrue(isUpdated);
        assertEquals(20.0, bookToUpdate.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookNotFound() {
        boolean isUpdated = bookService.updatePrice(999L, 20.0);
        
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRangeWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, booksInRange.size());
        assertEquals("Title1", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book bookToRemove = new Book("Test Title", "Test Author", 10.0);
        bookService.addBook(bookToRemove);
        
        boolean isRemoved = bookService.removeBook(bookToRemove.getId());
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(bookToRemove.getId()).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenBookNotFound() {
        boolean isRemoved = bookService.removeBook(999L);
        
        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooksWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenCollectionIsNotEmpty() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCountWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        int count = bookService.getBookCount();
        
        assertEquals(2, count);
    }
}
