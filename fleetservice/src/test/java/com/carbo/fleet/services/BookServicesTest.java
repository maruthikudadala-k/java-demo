
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
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        Book addedBook = bookService.addBook(book);
        
        assertNotNull(addedBook);
        assertEquals(book.getTitle(), addedBook.getTitle());
        assertEquals(book.getAuthor(), addedBook.getAuthor());
        assertEquals(book.getPrice(), addedBook.getPrice());
    }

    @Test
    void shouldReturnBookByIdWhenExists() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        Optional<Book> foundBook = bookService.findById(book.getId());
        
        assertTrue(foundBook.isPresent());
        assertEquals(book.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFoundById() {
        Optional<Book> foundBook = bookService.findById(999L);
        
        assertFalse(foundBook.isPresent());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author1", 15.0));
        bookService.addBook(new Book("Title3", "Author2", 20.0));
        
        List<Book> booksByAuthor = bookService.findByAuthor("Author1");
        
        assertEquals(2, booksByAuthor.size());
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean isUpdated = bookService.updatePrice(book.getId(), 15.0);
        
        assertTrue(isUpdated);
        assertEquals(15.0, bookService.findById(book.getId()).get().getPrice());
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean isUpdated = bookService.updatePrice(999L, 15.0);
        
        assertFalse(isUpdated);
    }

    @Test
    void shouldCalculateTotalValueOfBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        double totalValue = bookService.calculateTotalValue();
        
        assertEquals(30.0, totalValue);
    }

    @Test
    void shouldReturnBooksByPriceRange() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        bookService.addBook(new Book("Title3", "Author3", 30.0));
        
        List<Book> booksInRange = bookService.getBooksByPriceRange(10.0, 20.0);
        
        assertEquals(2, booksInRange.size());
    }

    @Test
    void shouldRemoveBookByIdSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);
        
        boolean isRemoved = bookService.removeBook(book.getId());
        
        assertTrue(isRemoved);
        assertFalse(bookService.findById(book.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean isRemoved = bookService.removeBook(999L);
        
        assertFalse(isRemoved);
    }

    @Test
    void shouldReturnAllBooks() {
        bookService.addBook(new Book("Title1", "Author1", 10.0));
        bookService.addBook(new Book("Title2", "Author2", 20.0));
        
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(2, allBooks.size());
    }

    @Test
    void shouldReturnTrueWhenBooksCollectionIsEmpty() {
        assertTrue(bookService.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenBooksCollectionIsNotEmpty() {
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        assertFalse(bookService.isEmpty());
    }

    @Test
    void shouldReturnCorrectBookCount() {
        assertEquals(0, bookService.getBookCount());
        
        bookService.addBook(new Book("Title", "Author", 10.0));
        
        assertEquals(1, bookService.getBookCount());
    }
}
