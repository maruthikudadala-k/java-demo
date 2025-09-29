
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    public void shouldAddBookWhenValidBookProvided() {
        Book book = new Book("Title", "Author", 10.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals("Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    public void shouldFindBookByIdWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Title", foundBook.get().getTitle());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    public void shouldReturnListOfBooksByAuthorWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> booksByAuthor = bookService.findByAuthor("Nonexistent Author");

        assertTrue(booksByAuthor.isEmpty());
    }

    @Test
    public void shouldUpdatePriceWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    public void shouldReturnFalseWhenUpdatingPriceOfNonexistentBook() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);

        assertFalse(isUpdated);
    }

    @Test
    public void shouldCalculateTotalValueWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    public void shouldReturnEmptyListWhenNoBooksExistInPriceRange() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        List<Book> booksInRange = bookService.getBooksByPriceRange(20.0, 30.0);

        assertTrue(booksInRange.isEmpty());
    }

    @Test
    public void shouldRemoveBookWhenBookExists() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    public void shouldReturnFalseWhenRemovingNonexistentBook() {
        boolean isRemoved = bookService.removeBook(999L);

        assertFalse(isRemoved);
    }

    @Test
    public void shouldReturnAllBooksWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    public void shouldReturnTrueWhenCollectionIsEmpty() {
        boolean isEmpty = bookService.isEmpty();

        assertTrue(isEmpty);
    }

    @Test
    public void shouldReturnFalseWhenCollectionIsNotEmpty() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        boolean isEmpty = bookService.isEmpty();

        assertFalse(isEmpty);
    }

    @Test
    public void shouldReturnBookCountWhenBooksExist() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        int bookCount = bookService.getBookCount();

        assertEquals(2, bookCount);
    }
}
