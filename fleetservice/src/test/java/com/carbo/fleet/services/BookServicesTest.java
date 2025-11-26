
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
        
        assertEquals(book, addedBook);
    }

    @Test
    public void shouldFindBookByIdSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);
        
        List<Book> foundBooks = bookService.findByAuthor("Test Author");
        
        assertFalse(foundBooks.isEmpty());
        assertEquals(1, foundBooks.size());
        assertEquals(book, foundBooks.get(0));
    }

    @Test
    public void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(1L, 15.0);
        
        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);
        
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertFalse(booksInRange.isEmpty());
        assertEquals(1, booksInRange.size());
        assertEquals(book, booksInRange.get(0));
    }

    @Test
    public void shouldRemoveBookByIdSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        boolean isRemoved = bookService.removeBook(1L);
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(999L);
        
        assertFalse(isRemoved);
    }

    @Test
    public void shouldGetAllBooksSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(1, allBooks.size());
        assertEquals(book, allBooks.get(0));
    }

    @Test
    public void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }
}
