package com.rodrigo.library_management_system.dto;

import com.rodrigo.library_management_system.entity.Book;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class BorrowResponse {

    Book book;

    String username;

}
