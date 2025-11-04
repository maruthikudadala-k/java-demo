
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    public void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Sample Book");
        when(book.getAuthor()).thenReturn("Author Name");
        when(book.getPrice()).thenReturn(10.0);
        
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Sample Book", addedBook.getTitle());
        assertEquals("Author Name", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldReturnBookWhenFoundById() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Book One", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        bookService.addBook(book1);
        Book book2 = new Book("Book Two", "Author One", 20.0);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author One");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldUpdatePriceWhenBookFound() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean updated = bookService.updatePrice(1L, 25.0);

        assertTrue(updated);
        assertEquals(25.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 25.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueOfBooks() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        bookService.addBook(book1);
        Book book2 = new Book("Book Two", "Author Two", 25.0);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(40.0, totalValue);
    }

    @Test
    public void shouldReturnBooksByPriceRange() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        bookService.addBook(book1);
        Book book2 = new Book("Book Two", "Author Two", 25.0);
        bookService.addBook(book2);

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Book One", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookWhenIdExists() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooks() {
        Book book1 = new Book("Book One", "Author One", 15.0);
        bookService.addBook(book1);
        Book book2 = new Book("Book Two", "Author Two", 25.0);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenServiceIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenServiceIsNotEmpty() {
        bookService.addBook(new Book("Book One", "Author One", 15.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnBookCount() {
        bookService.addBook(new Book("Book One", "Author One", 15.0));
        bookService.addBook(new Book("Book Two", "Author Two", 25.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
