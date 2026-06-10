package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.entity.BookHistory;
import com.rodrigo.library_management_system.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
public class BookHistoryController {

    private final LibraryService libraryService;

    public BookHistoryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<List<BookHistory>> getBookHistory(@PathVariable String isbn) {
        return ResponseEntity.ok(libraryService.getBookHistory(isbn));
    }

}
