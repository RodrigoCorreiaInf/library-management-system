package com.rodrigo.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateBookRequest {

    private String isbn;

    private String title;

    private String author;

}
