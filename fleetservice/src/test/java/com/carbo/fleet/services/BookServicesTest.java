
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
    public void shouldAddBookSuccessfully() {
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        when(book.getId()).thenReturn(1L);

        Book addedBook = bookService.addBook(book);

        assertEquals(book, addedBook);
        assertEquals(1, bookService.getBookCount());
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
    public void shouldFindBooksByAuthorSuccessfully() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author1", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> foundBooks = bookService.findByAuthor("Author1");

        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> foundBooks = bookService.findByAuthor("Unknown Author");

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getPrice()).thenReturn(10.0);
        bookService.addBook(book);

        boolean result = bookService.updatePrice(1L, 20.0);

        assertTrue(result);
        assertEquals(20.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean result = bookService.updatePrice(999L, 20.0);

        assertFalse(result);
    }

    @Test
    public void shouldCalculateTotalValueSuccessfully() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRangeSuccessfully() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> foundBooks = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksInPriceRange() {
        List<Book> foundBooks = bookService.getBooksByPriceRange(20.0, 30.0);

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        bookService.addBook(book);

        boolean result = bookService.removeBook(1L);

        assertTrue(result);
        assertEquals(0, bookService.getBookCount());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean result = bookService.removeBook(999L);

        assertFalse(result);
    }

    @Test
    public void shouldGetAllBooksSuccessfully() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        Book book1 = new Book("Title1", "Author1", 10.0);
        bookService.addBook(book1);

        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCountSuccessfully() {
        assertEquals(0, bookService.getBookCount());

        Book book1 = new Book("Title1", "Author1", 10.0);
        bookService.addBook(book1);

        assertEquals(1, bookService.getBookCount());
    }
}
