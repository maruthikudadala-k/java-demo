
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
    void shouldAddBookWhenValidData() {
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
    void shouldReturnBookWhenFoundById() {
        Book bookToFind = new Book("Test Title", "Test Author", 10.0);
        bookToFind.setId(1L);
        bookService.addBook(bookToFind);

        Optional<Book> foundBook = bookService.findById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(bookToFind.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Title 1", "Author A", 10.0));
        bookService.addBook(new Book("Title 2", "Author B", 15.0));
        bookService.addBook(new Book("Title 3", "Author A", 20.0));

        List<Book> booksByAuthor = bookService.findByAuthor("Author A");

        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdatePriceWhenBookExists() {
        Book bookToUpdate = new Book("Test Title", "Test Author", 10.0);
        bookToUpdate.setId(1L);
        bookService.addBook(bookToUpdate);

        boolean updated = bookService.updatePrice(1L, 20.0);

        assertTrue(updated);
        assertEquals(20.0, bookToUpdate.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);

        assertFalse(updated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Book 1", "Author A", 5.0));
        bookService.addBook(new Book("Book 2", "Author B", 15.0));

        double totalValue = bookService.calculateTotalValue();

        assertEquals(20.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        bookService.addBook(new Book("Book 1", "Author A", 5.0));
        bookService.addBook(new Book("Book 2", "Author B", 15.0));
        bookService.addBook(new Book("Book 3", "Author C", 25.0));

        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Book 2", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookWhenExists() {
        Book bookToRemove = new Book("Test Title", "Test Author", 10.0);
        bookToRemove.setId(1L);
        bookService.addBook(bookToRemove);

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
        Book book1 = new Book("Title 1", "Author A", 10.0);
        Book book2 = new Book("Title 2", "Author B", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldCheckIfBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
        bookService.addBook(new Book("Some Title", "Some Author", 10.0));
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnBookCount() {
        assertEquals(0, bookService.getBookCount());
        bookService.addBook(new Book("Some Title", "Some Author", 10.0));
        assertEquals(1, bookService.getBookCount());
    }
}
