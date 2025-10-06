
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void shouldReturnBookByIdWhenExists() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        
        bookService.addBook(book);
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals("Test Title", foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(1L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        
        bookService.addBook(book);
        boolean isUpdated = bookService.updatePrice(1L, 15.0);

        assertTrue(isUpdated);
        assertEquals(15.0, book.getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(1L, 15.0);
        
        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        double totalValue = bookService.calculateTotalValue();

        assertEquals(30.0, totalValue);
    }

    @Test
    void shouldReturnBooksInPriceRange() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        var booksInRange = bookService.getBooksByPriceRange(5.0, 15.0);

        assertEquals(1, booksInRange.size());
        assertEquals("Title1", booksInRange.get(0).getTitle());
    }

    @Test
    void shouldRemoveBookSuccessfully() {
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Title");
        when(book.getAuthor()).thenReturn("Test Author");
        when(book.getPrice()).thenReturn(10.0);
        
        bookService.addBook(book);
        boolean isRemoved = bookService.removeBook(1L);
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(1L).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(1L);
        
        assertFalse(isRemoved);
    }

    @Test
    void shouldReturnAllBooks() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        var allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenBooksAreEmpty() {
        boolean isEmpty = bookService.isEmpty();
        
        assertTrue(isEmpty);
    }

    @Test
    void shouldReturnFalseWhenBooksAreNotEmpty() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        bookService.addBook(book1);
        
        boolean isEmpty = bookService.isEmpty();
        
        assertFalse(isEmpty);
    }

    @Test
    void shouldReturnBookCount() {
        Book book1 = new Book("Title1", "Author1", 10.0);
        Book book2 = new Book("Title2", "Author2", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);
        
        int count = bookService.getBookCount();

        assertEquals(2, count);
    }
}
