package com.rodrigo.library_management_system.mcp;

import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.dto.UpdateBookRequest;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.entity.BookHistory;
import com.rodrigo.library_management_system.service.LibraryService;
import com.rodrigo.library_management_system.service.NaturalLanguageSearchService;

import org.springframework.ai.tool.annotation.Tool;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMcpTools {

    private final LibraryService libraryService;

    private final NaturalLanguageSearchService naturalLanguageSearchService;

    public BookMcpTools(LibraryService libraryService, NaturalLanguageSearchService naturalLanguageSearchService) {
        this.libraryService = libraryService;
        this.naturalLanguageSearchService = naturalLanguageSearchService;
    }

    // 2. APPLY BOTH ANNOTATIONS TO EVERY METHOD YOU WANT THE CHAT CLIENT TO USE
    @McpTool(description = "Fetch all books currently available in the library")
    @Tool(description = "Fetch all books currently available in the library")
    public List<Book> listBooks() {
        return libraryService.getAllBooks();
    }

    @McpTool(description = "Get full details of a book using its unique ISBN number")
    @Tool(description = "Get full details of a book using its unique ISBN number")
    public Book getBookByIsbn(String isbn) {
        return libraryService.getBookByIsbn(isbn);
    }

    @McpTool(description = "Search for books using exact matching on author and/or title fields")
    @Tool(description = "Search for books using exact matching on author and/or title fields")
    public List<Book> searchBooksExact(String author, String title) {
        return libraryService.searchBooks(author, title);
    }

    @McpTool(description = "Search the library using a conversational, natural language query")
    @Tool(description = "Search the library using a conversational, natural language query")
    public List<Book> searchBooksNatural(String query) {
        return naturalLanguageSearchService.search(query);
    }

    @McpTool(description = "Add a brand new book to the library catalog")
    @Tool(description = "Add a brand new book to the library catalog")
    public String addBook(CreateBookRequest dto) {
        if (dto.getIsbn() != null && libraryService.checkIfExists(dto.getIsbn())) {
            return "Conflict Error: A book with ISBN '" + dto.getIsbn() + "' already exists.";
        }
        Book savedBook = libraryService.addBook(dto);
        return "Success: Added " + savedBook.getTitle() + " to the library.";
    }

    @McpTool(description = "Update details of an existing book using its ISBN")
    @Tool(description = "Update details of an existing book using its ISBN")
    public String updateBook(String isbn, UpdateBookRequest dto) {
        libraryService.updateBook(isbn, dto);
        return "Success: Updated book with ISBN '" + isbn + "'.";
    }

    @McpTool(description = "Remove a book permanently from the system using its ISBN")
    @Tool(description = "Remove a book permanently from the system using its ISBN")
    public String removeBook(String isbn) {
        if (isbn != null && libraryService.checkIfExists(isbn)) {
            libraryService.removeBook(isbn);
            return "Success: The book '" + isbn + "' has been removed.";
        }
        return "Error: Book with ISBN '" + isbn + "' does not exist.";
    }

    @McpTool(description = "Check out/borrow a book. Requires the book's ISBN and the username of the reader.")
    @Tool(description = "Check out/borrow a book. Requires the book's ISBN and the username of the reader.")
    public BookHistory borrowBook(String isbn, String username) {
        return libraryService.borrowBook(isbn, username);
    }

    @McpTool(description = "Return a borrowed book back to the library using its ISBN")
    @Tool(description = "Return a borrowed book back to the library using its ISBN")
    public BookHistory returnBook(String isbn) {
        return libraryService.returnBook(isbn);
    }

}