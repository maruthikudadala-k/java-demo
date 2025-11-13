
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);

        Book addedBook = bookService.addBook(book);

        assertEquals(book, addedBook);
        assertNotNull(addedBook.getId());
        assertEquals(1, bookService.getBookCount());
    }

    @Test
    void shouldReturnBookWhenFoundById() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);
        when(book.getId()).thenReturn(1L);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author1");

        assertEquals(2, booksByAuthor.size());
        assertTrue(booksByAuthor.contains(book1));
        assertTrue(booksByAuthor.contains(book2));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> booksByAuthor = bookService.findByAuthor("NonExistingAuthor");

        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        when(book.getId()).thenReturn(1L);

        boolean isUpdated = bookService.updatePrice(1L, 20.0);

        assertTrue(isUpdated);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceForNonExistingBook() {
        boolean isUpdated = bookService.updatePrice(999L, 20.0);

        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValueWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    void shouldReturnZeroTotalValueWhenNoBooks() {
        double totalValue = bookService.calculateTotalValue();

        assertEquals(0.0, totalValue);
    }

    @Test
    void shouldReturnBooksByPriceRangeWhenBooksExist() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, booksInRange.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 20.0);

        assertTrue(booksInRange.isEmpty());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        when(book.getId()).thenReturn(1L);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistingBook() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    void shouldReturnAllBooksWhenCalled() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
        assertTrue(allBooks.contains(book1));
        assertTrue(allBooks.contains(book2));
    }

    @Test
    void shouldReturnTrueWhenNoBooks() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenBooksExist() {
        bookService.addBook(new Book("Title", "Author", 10.0));

        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCountWhenBooksAdded() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 15.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
