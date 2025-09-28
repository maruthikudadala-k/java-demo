
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book bookMock;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        when(bookMock.getTitle()).thenReturn("Effective Java");
        when(bookMock.getAuthor()).thenReturn("Joshua Bloch");
        when(bookMock.getPrice()).thenReturn(45.0);

        Book addedBook = bookService.addBook(bookMock);

        assertNotNull(addedBook);
        assertEquals("Effective Java", addedBook.getTitle());
        assertEquals("Joshua Bloch", addedBook.getAuthor());
        assertEquals(45.0, addedBook.getPrice());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        Book book = new Book("Java Concurrency in Practice", "Brian Goetz", 50.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Java Concurrency in Practice", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(99L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthorWhenExists() {
        Book book1 = new Book("Clean Code", "Robert C. Martin", 40.0);
        Book book2 = new Book("The Pragmatic Programmer", "Andrew Hunt", 30.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Robert C. Martin");

        assertEquals(1, booksByAuthor.size());
        assertEquals("Clean Code", booksByAuthor.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> booksByAuthor = bookService.findByAuthor("Unknown Author");
        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Design Patterns", "Erich Gamma", 55.0);
        book.setId(2L);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(2L, 60.0);

        assertTrue(updated);
        assertEquals(60.0, book.getPrice());
    }

    @Test
    void shouldNotUpdatePriceWhenBookNotFound() {
        boolean updated = bookService.updatePrice(99L, 60.0);
        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book A", "Author A", 20.0));
        bookService.addBook(new Book("Book B", "Author B", 30.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(50.0, totalValue);
    }

    @Test
    void shouldReturnBooksByPriceRangeWhenExists() {
        bookService.addBook(new Book("Affordable Book", "Author A", 15.0));
        bookService.addBook(new Book("Expensive Book", "Author B", 100.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 50.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Affordable Book", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book book = new Book("To Be Removed", "Author C", 25.0);
        book.setId(3L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(3L);

        assertTrue(removed);
        assertFalse(bookService.findById(3L).isPresent());
    }

    @Test
    void shouldNotRemoveBookWhenNotFound() {
        boolean removed = bookService.removeBook(99L);
        assertFalse(removed);
    }

    @Test
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book 1", "Author A", 10.0));
        bookService.addBook(new Book("Book 2", "Author B", 20.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldCheckIfBooksAreEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Book", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Book", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
