
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
    private BookService.Book book;

    @Test
    void shouldAddBookSuccessfully() {
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
    void shouldReturnBookById() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book1.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        Book book2 = new Book("Title 2", "Author 1", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> booksByAuthor = bookService.findByAuthor("Author 1");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean updated = bookService.updatePrice(1L, 20.0);

        assertTrue(updated);
        assertEquals(20.0, bookService.findById(1L).get().getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);

        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        Book book2 = new Book("Title 2", "Author 2", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertEquals(40.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        Book book2 = new Book("Title 2", "Author 2", 25.0);
        book3 = new Book("Title 3", "Author 3", 30.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);

        List<Book> booksInRange = bookService.getBooksByPriceRange(15.0, 25.0);

        assertEquals(2, booksInRange.size());
    }

    @Test
    void shouldRemoveBookById() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        book1.setId(1L);
        bookService.addBook(book1);

        boolean removed = bookService.removeBook(1L);

        assertTrue(removed);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertFalse(removed);
    }

    @Test
    void shouldReturnAllBooks() {
        Book book1 = new Book("Title 1", "Author 1", 15.0);
        Book book2 = new Book("Title 2", "Author 2", 25.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldCheckIfBookCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());

        Book book1 = new Book("Title 1", "Author 1", 15.0);
        bookService.addBook(book1);

        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());

        Book book1 = new Book("Title 1", "Author 1", 15.0);
        bookService.addBook(book1);

        assertEquals(1, bookService.getBookCount());
    }
}
