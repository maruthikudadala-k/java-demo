
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookService.Book book;

    @Test
    void shouldAddBookWhenValidBookProvided() {
        when(book.getTitle()).thenReturn("Sample Title");
        when(book.getAuthor()).thenReturn("Sample Author");
        when(book.getPrice()).thenReturn(10.0);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Sample Title", addedBook.getTitle());
        assertEquals("Sample Author", addedBook.getAuthor());
        assertEquals(10.0, addedBook.getPrice());
    }

    @Test
    void shouldReturnBookWhenBookExistsById() {
        Book bookToFind = new Book("Sample Title", "Sample Author", 10.0);
        bookToFind.setId(1L);
        bookService.addBook(bookToFind);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Sample Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookDoesNotExistById() {
        Optional<Book> foundBook = bookService.findById(99L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthorWhenExists() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author A", 15.0));
        bookService.addBook(new Book("Book Three", "Author B", 20.0));

        List<Book> booksByAuthor = bookService.findByAuthor("Author A");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book bookToUpdate = new Book("Sample Title", "Sample Author", 10.0);
        bookToUpdate.setId(1L);
        bookService.addBook(bookToUpdate);

        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, bookToUpdate.getPrice());
    }

    @Test
    void shouldNotUpdatePriceWhenBookDoesNotExist() {
        boolean isUpdated = bookService.updatePrice(99L, 15.0);

        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author A", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(25.0, totalValue);
    }

    @Test
    void shouldReturnBooksWithinPriceRange() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author A", 20.0));
        bookService.addBook(new Book("Book Three", "Author B", 30.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Book Two", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book bookToRemove = new Book("Sample Title", "Sample Author", 10.0);
        bookToRemove.setId(1L);
        bookService.addBook(bookToRemove);

        boolean isRemoved = bookService.removeBook(1L);

        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldNotRemoveBookWhenDoesNotExist() {
        boolean isRemoved = bookService.removeBook(99L);

        assertFalse(isRemoved);
    }

    @Test
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author A", 15.0));

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenBooksAreEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenBooksAreNotEmpty() {
        bookService.addBook(new Book("Sample Title", "Sample Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnCountOfBooks() {
        bookService.addBook(new Book("Book One", "Author A", 10.0));
        bookService.addBook(new Book("Book Two", "Author A", 15.0));

        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
