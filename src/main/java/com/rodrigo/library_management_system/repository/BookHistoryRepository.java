package com.rodrigo.library_management_system.repository;

import com.rodrigo.library_management_system.entity.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookHistoryRepository extends JpaRepository<BookHistory, UUID> {

    Optional<BookHistory> findFirstByBookIsbnAndReturnedAtIsNull(String isbn);

    List<BookHistory> findByBookIsbn(String isbn);
}
