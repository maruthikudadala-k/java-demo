
package com.carbo.fleet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);

        Book addedBook = bookService.addBook(book);

        assertThat(addedBook).isNotNull();
        assertThat(addedBook.getTitle()).isEqualTo("Title");
        assertThat(addedBook.getAuthor()).isEqualTo("Author");
        assertThat(addedBook.getPrice()).isEqualTo(10.0);
    }

    @Test
    void shouldFindBookById() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        Optional<Book> foundBook = bookService.findById(1L);

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Title");
    }

    @Test
    void shouldReturnEmptyOptionalWhenBookNotFound() {
        Optional<Book> foundBook = bookService.findById(999L);

        assertThat(foundBook).isNotPresent();
    }

    @Test
    void shouldFindBooksByAuthor() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> books = bookService.findByAuthor("Author");

        assertThat(books).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksByAuthor() {
        List<Book> books = bookService.findByAuthor("Unknown Author");

        assertThat(books).isEmpty();
    }

    @Test
    void shouldUpdateBookPriceSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean updated = bookService.updatePrice(1L, 20.0);

        assertThat(updated).isTrue();
        assertThat(book.getPrice()).isEqualTo(20.0);
    }

    @Test
    void shouldReturnFalseWhenUpdatingPriceOfNonExistentBook() {
        boolean updated = bookService.updatePrice(999L, 20.0);

        assertThat(updated).isFalse();
    }

    @Test
    void shouldCalculateTotalValue() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        double totalValue = bookService.calculateTotalValue();

        assertThat(totalValue).isEqualTo(25.0);
    }

    @Test
    void shouldGetBooksByPriceRange() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 20.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> books = bookService.getBooksByPriceRange(5.0, 15.0);

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Title1");
    }

    @Test
    void shouldRemoveBookSuccessfully() {
        Book book = new Book("Title", "Author", 10.0);
        book.setId(1L);
        bookService.addBook(book);

        boolean removed = bookService.removeBook(1L);

        assertThat(removed).isTrue();
        assertThat(bookService.getBookCount()).isEqualTo(0);
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBook() {
        boolean removed = bookService.removeBook(999L);

        assertThat(removed).isFalse();
    }

    @Test
    void shouldGetAllBooks() {
        Book book1 = new Book("Title1", "Author", 10.0);
        Book book2 = new Book("Title2", "Author", 15.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        List<Book> allBooks = bookService.getAllBooks();

        assertThat(allBooks).hasSize(2);
    }

    @Test
    void shouldCheckIfServiceIsEmpty() {
        boolean isEmpty = bookService.isEmpty();

        assertThat(isEmpty).isTrue();
    }

    @Test
    void shouldReturnBookCount() {
        Book book = new Book("Title", "Author", 10.0);
        bookService.addBook(book);

        int count = bookService.getBookCount();

        assertThat(count).isEqualTo(1);
    }
}
