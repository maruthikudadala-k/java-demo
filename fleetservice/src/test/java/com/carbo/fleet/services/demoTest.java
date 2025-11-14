
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
    public void shouldReturnAddedBookWhenAddBookCalled() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(1L);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenFindByIdCalledWithNonExistingId() {
        Optional<Book> bookOpt = bookService.findById(99L);
        assertFalse(bookOpt.isPresent());
    }

    @Test
    public void shouldReturnListOfBooksWhenFindByAuthorCalled() {
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> booksByAuthor = bookService.findByAuthor("Test Author");

        assertNotNull(booksByAuthor);
        assertEquals(1, booksByAuthor.size());
        assertEquals("Test Title", booksByAuthor.get(0).getTitle());
    }

    @Test
    public void shouldReturnTrueWhenUpdatePriceCalledWithExistingBook() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean updated = bookService.updatePrice(bookId, 15.0);

        assertTrue(updated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnTotalValueWhenCalculateTotalValueCalled() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(10.0, totalValue);
    }

    @Test
    public void shouldReturnListOfBooksWhenGetBooksByPriceRangeCalled() {
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertNotNull(booksInRange);
        assertEquals(1, booksInRange.size());
    }

    @Test
    public void shouldReturnTrueWhenRemoveBookCalledWithExistingId() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        Long bookId = book.getId();

        boolean removed = bookService.removeBook(bookId);

        assertTrue(removed);
    }

    @Test
    public void shouldReturnAllBooksWhenGetAllBooksCalled() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        List<Book> allBooks = bookService.getAllBooks();

        assertNotNull(allBooks);
        assertEquals(1, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenIsEmptyCalledOnEmptyService() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCountWhenGetBookCountCalled() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        int count = bookService.getBookCount();

        assertEquals(1, count);
    }
}
