package com.rodrigo.library_management_system.service;

import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class LibraryService {

    private final BookRepository bookRepository;

    public Book addBook(CreateBookRequest request) {
        Book book = new Book(request.getIsbn(), request.getTitle(), request.getAuthor());
        return bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }


    public boolean checkIfExists(String id) {
        return bookRepository.existsById(id);
    }


}
