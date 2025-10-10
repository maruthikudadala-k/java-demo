
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
    private List<BookService.Book> books;

    @Test
    public void shouldAddBookWhenValidBookIsProvided() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        when(books.add(book)).thenReturn(true);
        
        BookService.Book result = bookService.addBook(book);
        
        assertEquals(book, result);
        verify(books).add(book);
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        book.setId(1L);
        when(books.stream()).thenReturn(Stream.of(book));
        
        Optional<BookService.Book> result = bookService.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        when(books.stream()).thenReturn(Stream.empty());
        
        Optional<BookService.Book> result = bookService.findById(1L);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        when(books.stream()).thenReturn(Stream.of(book));
        
        List<BookService.Book> result = bookService.findByAuthor("Author");
        
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        book.setId(1L);
        when(books.stream()).thenReturn(Stream.of(book));
        
        boolean result = bookService.updatePrice(1L, 15.0);
        
        assertTrue(result);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonexistentBook() {
        when(books.stream()).thenReturn(Stream.empty());
        
        boolean result = bookService.updatePrice(1L, 15.0);
        
        assertFalse(result);
    }

    @Test
    public void shouldReturnTotalValueOfBooks() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 20.0);
        when(books.stream()).thenReturn(Stream.of(book1, book2));
        
        double result = bookService.calculateTotalValue();
        
        assertEquals(30.0, result);
    }

    @Test
    public void shouldReturnBooksWithinPriceRange() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 20.0);
        when(books.stream()).thenReturn(Stream.of(book1, book2));
        
        List<BookService.Book> result = bookService.getBooksByPriceRange(5.0, 15.0);
        
        assertEquals(1, result.size());
        assertEquals(book1, result.get(0));
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        book.setId(1L);
        when(books.stream()).thenReturn(Stream.of(book));
        
        boolean result = bookService.removeBook(1L);
        
        assertTrue(result);
        verify(books).remove(book);
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        when(books.stream()).thenReturn(Stream.empty());
        
        boolean result = bookService.removeBook(1L);
        
        assertFalse(result);
    }

    @Test
    public void shouldReturnAllBooks() {
        BookService.Book book = new BookService.Book("Title", "Author", 10.0);
        when(books.stream()).thenReturn(Stream.of(book));
        
        List<BookService.Book> result = bookService.getAllBooks();
        
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    public void shouldCheckIfBooksAreEmpty() {
        when(books.stream()).thenReturn(Stream.empty());
        
        boolean result = bookService.isEmpty();
        
        assertTrue(result);
    }

    @Test
    public void shouldReturnBookCount() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 20.0);
        when(books.stream()).thenReturn(Stream.of(book1, book2));
        
        int result = bookService.getBookCount();
        
        assertEquals(2, result);
    }
}
