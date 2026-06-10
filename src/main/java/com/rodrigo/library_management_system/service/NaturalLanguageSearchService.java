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
                Convert the user's request into:
                - title
                - author

                User query:
                %s

                Return as a BookSearchCriteria object only.
                """.formatted(userQuery);

        BookSearchCriteria response = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(BookSearchCriteria.class);

        if (response == null) {
            return bookRepository.findAll();
        }

        return bookRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(response.getAuthor(), response.getTitle());
    }

}
