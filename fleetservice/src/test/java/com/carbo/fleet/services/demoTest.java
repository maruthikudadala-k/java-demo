
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);
        assertEquals(1, bookService.getBookCount());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        Optional<BookService.Book> foundBook = bookService.findById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<BookService.Book> foundBook = bookService.findById(1L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<BookService.Book> booksByAuthor = bookService.findByAuthor("Author1");
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        BookService.Book book = new BookService.Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        boolean isUpdated = bookService.updatePrice(1L, 20.0);
        assertTrue(isUpdated);
        assertEquals(20.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatePriceForNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(1L, 20.0);
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        double totalValue = bookService.calculateTotalValue();
        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<BookService.Book> booksInRange = bookService.getBooksByPriceRange(10.0, 15.0);
        assertEquals(1, booksInRange.size());
        assertEquals("Title1", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        BookService.Book book = new BookService.Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        boolean isRemoved = bookService.removeBook(1L);
        assertTrue(isRemoved);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(1L);
        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<BookService.Book> allBooks = bookService.getAllBooks();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenCollectionIsNotEmpty() {
        BookService.Book book = new BookService.Book("Test Title", "Test Author", 10.0);
        bookService.addBook(book);
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCorrectBookCount() {
        BookService.Book book1 = new BookService.Book("Title1", "Author1", 10.0);
        BookService.Book book2 = new BookService.Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        assertEquals(2, bookService.getBookCount());
    }
}
