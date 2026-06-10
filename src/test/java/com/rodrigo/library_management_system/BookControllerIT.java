package com.rodrigo.library_management_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.library_management_system.BaseIntegrationTest;
import com.rodrigo.library_management_system.dto.CreateBookRequest;
import com.rodrigo.library_management_system.dto.UpdateBookRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.hamcrest.Matchers.*;

class BookControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==========================================
    // POST /books (Add Book) -> Requires OWNER
    // ==========================================

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldAddBookSuccessfully() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        CreateBookRequest request = new CreateBookRequest();
        request.setIsbn(uniqueIsbn);
        request.setTitle("Clean Architecture");
        request.setAuthor("Robert C. Martin");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(uniqueIsbn));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldReturnConflictWhenAddingExistingBook() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        CreateBookRequest request = new CreateBookRequest();
        request.setIsbn(uniqueIsbn);
        request.setTitle("Duplicate Book");
        request.setAuthor("Author");

        // Seed the first copy
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Duplicate attempt forces a 409 regardless of background state
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("already exists in the system")));
    }

    // ==========================================
    // GET /books -> permitAll() (No Auth Required)
    // ==========================================

    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldGetBookByIsbn() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, "Find Me Unique");

        mockMvc.perform(get("/books/" + uniqueIsbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Find Me Unique"));
    }

    @Test
    void shouldSearchBooksByFilters() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        String uniqueTitle = "Searchable Title " + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, uniqueTitle);

        mockMvc.perform(get("/books/search")
                        .param("title", uniqueTitle)
                        .param("author", "Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].isbn").value(uniqueIsbn));
    }

    // ==========================================
    // PUT /books/{isbn} (Update Book) -> Requires OWNER
    // ==========================================

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldUpdateBookSuccessfully() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, "Old Title");

        UpdateBookRequest updateReq = new UpdateBookRequest();
        updateReq.setTitle("Updated Title");
        updateReq.setAuthor("Updated Author");

        mockMvc.perform(put("/books/" + uniqueIsbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success: You have updated the book with isbn '" + uniqueIsbn + "'.")));
    }

    // ==========================================
    // DELETE /books (Remove Book) -> Requires OWNER
    // ==========================================

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldRemoveBookSuccessfully() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, "To Be Deleted");

        mockMvc.perform(delete("/books")
                        .param("isbn", uniqueIsbn))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has been removed from the system")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void shouldReturnConflictWhenDeletingNonExistentBook() throws Exception {
        String nonExistentIsbn = "NON-EXIST-" + UUID.randomUUID();

        mockMvc.perform(delete("/books")
                        .param("isbn", nonExistentIsbn))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("doesn't exist in the system")));
    }

    // ==========================================
    // POST /books/{isbn}/borrow & /return -> Requires CLIENT
    // ==========================================

    @Test
    @WithMockUser(roles = "CLIENT")
    void shouldBorrowBookSuccessfully() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, "Borrowable Book");

        mockMvc.perform(post("/books/" + uniqueIsbn + "/borrow"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void shouldReturnBookSuccessfully() throws Exception {
        String uniqueIsbn = "ISBN-" + UUID.randomUUID();
        setupExistingBook(uniqueIsbn, "Returnable Book");

        // Borrow it fresh within this execution block
        mockMvc.perform(post("/books/" + uniqueIsbn + "/borrow"));

        // Return it safely
        mockMvc.perform(post("/books/" + uniqueIsbn + "/return"))
                .andExpect(status().isOk());
    }

    // ==========================================
    // Helper Setup Tools
    // ==========================================

    private void setupExistingBook(String isbn, String title) throws Exception {
        CreateBookRequest createReq = new CreateBookRequest();
        createReq.setIsbn(isbn);
        createReq.setTitle(title);
        createReq.setAuthor("Author");

        mockMvc.perform(post("/books")
                .with(user("admin").roles("OWNER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)));
    }

}