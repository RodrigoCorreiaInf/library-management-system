package com.rodrigo.library_management_system.entity;

import com.rodrigo.library_management_system.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
