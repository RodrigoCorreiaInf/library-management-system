package com.rodrigo.library_management_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book_histories")
public class BookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_isbn")
    private Book book;

    @Column(name = "borrowed_by", nullable = false)
    private String borrowedBy;

    @Column(name = "borrowed_at", nullable = false)
    private LocalDateTime borrowedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "returned_late", nullable = false)
    private boolean returnedLate = false;

    public BookHistory(Book book, String borrowedBy, LocalDateTime borrowedAt) {
        this.book = book;
        this.borrowedBy = borrowedBy;
        this.borrowedAt = borrowedAt;
    }

}
