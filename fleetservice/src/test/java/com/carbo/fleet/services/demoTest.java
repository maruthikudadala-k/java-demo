
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
    public void shouldFindBookByIdSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(99L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthorSuccessfully() {
        when(book.getAuthor()).thenReturn("Test Author");
        bookService.addBook(book);

        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");

        assertEquals(1, booksByAuthor.size());
        assertEquals("Test Author", booksByAuthor.get(0).getAuthor());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(99L, 15.0);
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, booksInRange.size());
        assertEquals(10.0, booksInRange.get(0).getPrice());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(99L);
        assertFalse(isRemoved);
    }

    @Test
    public void shouldGetAllBooksSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(1, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmptySuccessfully() {
        assertTrue(bookService.isEmpty());

        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());

        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        assertEquals(1, bookService.getBookCount());
    }
}
