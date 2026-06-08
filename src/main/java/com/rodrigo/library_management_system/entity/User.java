package com.rodrigo.library_management_system.entity;

import com.rodrigo.library_management_system.valueobj.Role;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
