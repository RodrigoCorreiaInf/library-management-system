package com.rodrigo.library_management_system.entity;


import com.rodrigo.library_management_system.valueobj.BookStatus;
import jakarta.persistence.Id;

public class Book {

    @Id
    private Long id;

    private String title;

    private String author;

    private BookStatus available;

}
