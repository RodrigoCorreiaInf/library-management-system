package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.dto.NaturalSearchRequest;
import com.rodrigo.library_management_system.dto.UpdateBookRequest;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.entity.BookHistory;
import com.rodrigo.library_management_system.service.LibraryService;
import com.rodrigo.library_management_system.service.NaturalLanguageSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final LibraryService libraryService;

    private final NaturalLanguageSearchService naturalLanguageSearchService;

    public BookController(LibraryService libraryService, NaturalLanguageSearchService naturalLanguageSearchService) {
        this.libraryService = libraryService;
        this.naturalLanguageSearchService = naturalLanguageSearchService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(libraryService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return new ResponseEntity<>(libraryService.getBookByIsbn(isbn), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false) String author,
                                                  @RequestParam(required = false) String title) {
        return new ResponseEntity<>(libraryService.searchBooks(author, title), HttpStatus.OK);
    }

    @GetMapping("/search/natural")
    public ResponseEntity<List<Book>> searchBooksNatural(@RequestParam String query) {
        return new ResponseEntity<>(naturalLanguageSearchService.search(query), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody CreateBookRequest dto) {
        if (dto.getIsbn() != null && libraryService.checkIfExists(dto.getIsbn())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: A book with ID '" + dto.getIsbn() + "' already exists in the system.");
        }

        return new ResponseEntity<>(libraryService.addBook(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateBook(@RequestBody UpdateBookRequest dto, @PathVariable String isbn) {
        libraryService.updateBook(isbn, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Success: You have updated the book with isbn '" + isbn + "'.");
    }

    @DeleteMapping
    public ResponseEntity<?> removeBook(@RequestParam String isbn) {
        if (isbn != null && libraryService.checkIfExists(isbn)) {
            libraryService.removeBook(isbn);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Success: The book  '" + isbn + "' has been removed from the system.");
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Error: A book with ID '" + isbn + " 'doesn't exist in the system.");
    }

    @PostMapping("/{isbn}/borrow")
    public ResponseEntity<BookHistory> borrowBook(@PathVariable String isbn, Principal principal) {
        return ResponseEntity.ok(libraryService.borrowBook(isbn, principal.getName()));
    }

    @PostMapping("/{isbn}/return")
    public ResponseEntity<BookHistory> returnBook(@PathVariable String isbn) {
        return ResponseEntity.ok(libraryService.returnBook(isbn));
    }

}
