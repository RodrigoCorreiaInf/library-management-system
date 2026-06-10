package com.rodrigo.library_management_system.repository;

import com.rodrigo.library_management_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("""
            SELECT b
            FROM Book b
            WHERE (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))
            """)
    List<Book> searchBooksBy(String author);

}
