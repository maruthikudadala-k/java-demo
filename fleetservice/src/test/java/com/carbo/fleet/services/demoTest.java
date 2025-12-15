
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

    @Test
    public void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
        assertNotNull(addedBook.getId());
    }

    @Test
    public void shouldReturnBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(book.getId());

        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> foundBooks = bookService.findByAuthor("Author");

        assertEquals(2, foundBooks.size());
    }

    @Test
    public void shouldUpdatePriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean isUpdated = bookService.updatePrice(book.getId(), 12.0);

        assertTrue(isUpdated);
        assertEquals(12.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(999L, 12.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValue() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldGetBooksByPriceRange() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 15.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Title1", booksInRange.get(0).getTitle());
    }

    @Test
    public void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        boolean isRemoved = bookService.removeBook(book.getId());

        assertTrue(isRemoved);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldGetAllBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldCheckIfBookCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    public void shouldGetBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Title", "Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
