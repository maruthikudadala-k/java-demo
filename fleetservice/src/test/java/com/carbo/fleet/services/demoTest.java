
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
    public void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        verify(book, times(1)).setId(anyLong());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Long id = 1L;
        when(book.getId()).thenReturn(id);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(id);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyWhenNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author1");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Long id = 1L;
        Book book = new Book("Title", "Author", 10.0);
        book.setId(id);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(id, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);
        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Book1", "Author1", 10.0));
        bookService.addBook(new Book("Book2", "Author2", 20.0));
        bookService.addBook(new Book("Book3", "Author3", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(2, booksInRange.size());
    }

    @Test
    public void shouldRemoveBookWhenExists() {
        Long id = 1L;
        Book book = new Book("Title", "Author", 10.0);
        book.setId(id);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(id);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(id).isPresent());
    }

    @Test
    public void shouldNotRemoveBookWhenNotExists() {
        boolean isRemoved = bookService.removeBook(999L);
        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
