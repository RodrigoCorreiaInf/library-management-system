package com.rodrigo.library_management_system.tools;

import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookTools {

    private final LibraryService libraryService;

    @Tool(description = "Get all available books")
    public List<Book> getAvailableBooks() {
        return libraryService.getAllBooks();
    }

    @Tool(description = "Search books by author and/or title")
    public List<Book> searchBooks(String author, String title) {
        return libraryService.searchBooks(author, title);
    }

    @Tool(description = "Borrow a book")
    public String borrowBook(String isbn, String username) {
        libraryService.borrowBook(isbn, username);
        return "Book borrowed successfully";
    }

}
