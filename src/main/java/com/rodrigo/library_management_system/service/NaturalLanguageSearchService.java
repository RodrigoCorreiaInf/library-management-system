package com.rodrigo.library_management_system.service;

import com.rodrigo.library_management_system.dto.BookSearchCriteria;
import com.rodrigo.library_management_system.entity.Book;
import com.rodrigo.library_management_system.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NaturalLanguageSearchService {

    private final ChatClient chatClient;

    private final BookRepository bookRepository;

    public NaturalLanguageSearchService(ChatClient.Builder chatClientBuilder, BookRepository bookRepository) {
        this.chatClient = chatClientBuilder.build();
        this.bookRepository = bookRepository;
    }

    public List<Book> search(String userQuery) {
        String prompt = """
                Extract the search criteria from the user's request.
                User query: %s
                """.formatted(userQuery);

        BookSearchCriteria criteria = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(BookSearchCriteria.class); // Spring AI injects JSON instructions here

        if (criteria == null) {
            return bookRepository.findAll();
        }

        String author = (criteria.getAuthor() == null || criteria.getAuthor().isBlank()) ? null : criteria.getAuthor().trim();
        String title = (criteria.getTitle() == null || criteria.getTitle().isBlank()) ? null : criteria.getTitle().trim();

        IO.println(author);
        IO.println(title);

        if (author == null && title == null) {
            return bookRepository.findAll();
        }

        if (author != null && title != null) {
            return bookRepository.findByAuthorContainingIgnoreCaseAndTitleContainingIgnoreCase(author, title);
        } else if (author != null) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        } else {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
    }

}
