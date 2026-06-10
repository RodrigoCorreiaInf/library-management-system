package com.rodrigo.library_management_system.service;

import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.dto.UpdateBookRequest;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.entity.BookHistory;
import com.rodrigo.library_management_system.repository.BookHistoryRepository;
import com.rodrigo.library_management_system.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class LibraryService {

    private final BookRepository bookRepository;

    private final BookHistoryRepository bookHistoryRepository;

    private static final long MAX_BORROW_DAYS = 14;

    public Book addBook(CreateBookRequest dto) {
        Book book = new Book(dto.getIsbn(), dto.getTitle(), dto.getAuthor());
        return bookRepository.save(book);
    }

    public void updateBook(String isbn, UpdateBookRequest dto) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> searchBooks(String author) {
        return bookRepository.searchBooksBy(author);
    }

    public void removeBook(String isbn) {
        bookRepository.deleteById(isbn);
    }


    public boolean checkIfExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    public BookHistory borrowBook(String isbn, String clientName) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        book.setAvailable(false);

        BookHistory history = new BookHistory(book, clientName, LocalDateTime.now());
        return bookHistoryRepository.save(history);
    }

    public BookHistory returnBook(String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        BookHistory history = bookHistoryRepository.findFirstByBookIsbnAndReturnedAtIsNull(isbn)
                .orElseThrow(() -> new IllegalStateException("No active rental record found for this book"));

        LocalDateTime returnTime = LocalDateTime.now();
        history.setReturnedAt(returnTime);

        if (history.getBorrowedAt().plusDays(MAX_BORROW_DAYS).isBefore(returnTime)) {
            history.setReturnedLate(true);
        }

        book.setAvailable(true);

        return bookHistoryRepository.save(history);
    }

    public List<BookHistory> getBookHistory(String isbn) {
        return bookHistoryRepository.findByBookIsbn(isbn);
    }

}
