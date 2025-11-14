
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
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
        
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Test Title", addedBook.getTitle());
        assertEquals("Test Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindByIdWhenBookExists() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    public void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author1");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldReturnEmptyListWhenAuthorNotFound() {
        List<Book> booksByAuthor = bookService.findByAuthor("Unknown Author");

        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(book.getId(), 20.0);

        assertTrue(updated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(99L, 20.0);

        assertFalse(updated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        bookService.addBook(book1);

        List<Book> booksInRange = bookService.getBooksByPriceRange(20.0, 30.0);

        assertTrue(booksInRange.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(book.getId());

        assertTrue(removed);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(99L);

        assertFalse(removed);
    }

    @Test
    public void shouldReturnAllBooksWhenCalled() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    public void shouldReturnFalseWhenCollectionHasBooks() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldReturnCorrectBookCount() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        int bookCount = bookService.getBookCount();

        assertEquals(2, bookCount);
    }
}
