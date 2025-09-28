// ===== Current file: src\main\java\com\carbo\fleet\controllers\demo.java =====
package com.carbo.fleet.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Sample BookService for testing unit test generation
 * This service contains various method patterns that should generate comprehensive tests
 */
@Service
public class BookService {

    private List<Book> books = new ArrayList<>();

    /**
     * Adds a new book to the collection
     * @param book the book to add
     * @return the added book
     * @throws IllegalArgumentException if book is null or has invalid data
     */
    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }
        if (book.getPrice() < 0) {
            throw new IllegalArgumentException("Book price cannot be negative");
        }

        book.setId(generateNewId());
        books.add(book);
        return book;
    }

    /**
     * Finds a book by ID
     * @param id the book ID
     * @return Optional containing the book if found, empty otherwise
     */
    public Optional<Book> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return books.stream()
                .filter(book -> id.equals(book.getId()))
                .findFirst();
    }

    /**
     * Finds books by author
     * @param author the author name
     * @return list of books by the author
     */
    public List<Book> findByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return books.stream()
                .filter(book -> author.equalsIgnoreCase(book.getAuthor()))
                .toList();
    }

    /**
     * Updates a book's price
     * @param id the book ID
     * @param newPrice the new price
     * @return true if updated successfully, false if book not found
     * @throws IllegalArgumentException if price is negative
     */
    public boolean updatePrice(Long id, double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        Optional<Book> bookOpt = findById(id);
        if (bookOpt.isPresent()) {
            bookOpt.get().setPrice(newPrice);
            return true;
        }
        return false;
    }

    /**
   
     * @return total value
     */
    public double calculateTotalValue() {
        return books.stream()
                .mapToDouble(Book::getPrice)
                .sum();
    }

    /**
     * Gets books within a price range
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of books in price range
     */
    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        
        return books.stream()
                .filter(book -> book.getPrice() >= minPrice && book.getPrice() <= maxPrice)
                .toList();
    }

    /**
     * Removes a book by ID
     * @param id the book ID to remove
     * @return true if removed, false if not found
     */
    public boolean removeBook(Long id) {
        if (id == null) {
            return false;
        }
        return books.removeIf(book -> id.equals(book.getId()));
    }

    /**
     * Gets all books
     * @return copy of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Checks if collection is empty
     * @return true if no books, false otherwise
     */
    public boolean isEmpty() {
        return books.isEmpty();
    }

    /**
     * Gets count of books
     * @return number of books
     */
    public int getBookCount() {
        return books.size();
    }

    private Long generateNewId() {
        return books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0L) + 1L;
    }

    
    public static class Book {
        private Long id;
        private String title;
        private String author;
        private double price;
        private String isbn;

        public Book() {}

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }

        public Book(String title, String author, double price, String isbn) {
            this.title = title;
            this.author = author;
            this.price = price;
            this.isbn = isbn;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        @Override
        public String toString() {
            return "Book{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", price=" + price +
                    ", isbn='" + isbn + '\'' +
                    '}';
        }
    }
}
