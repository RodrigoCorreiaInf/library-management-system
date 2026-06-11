package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.mcp.BookMcpTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory; // Import the core interface
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder,
                          BookMcpTools bookMcpTools,
                          ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an intelligent library assistant running inside a Docker cluster.
                        You have full access to tools to manage inventory, search books, and process loans.
                        Always use your tools when a user asks you to list, search, add, update, borrow, or return books.
                        If a tool execution fails or returns an error, explain the issue clearly to the user.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(bookMcpTools)
                .build();
    }

    @PostMapping
    public String chat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String sessionId) {

        return this.chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }

}
