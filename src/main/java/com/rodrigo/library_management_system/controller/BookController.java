package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.service.LibraryService;
import com.rodrigo.library_management_system.valueobj.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<?> addBook(
            @RequestHeader("X-User-Role") Role role,
            @RequestBody CreateBookRequest request) {
//        validateRole(role, Role.OWNER);
        if (request.getIsbn() != null && libraryService.checkIfExists(request.getIsbn())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: A book with ID '" + request.getIsbn() + "' already exists in the system.");
        }

        return new ResponseEntity<>(libraryService.addBook(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(libraryService.getBooks(), HttpStatus.OK);
    }

}
