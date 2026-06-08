package com.rodrigo.library_management_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

@Entity
public class BorrowRecord {

    @Id
    private Long id;

    @ManyToOne
    private User borrower;

    private Book book;

    private Instant borrowedAt;

    private Instant dueDate;

    private Instant returnedAt;

    private boolean late;

}
