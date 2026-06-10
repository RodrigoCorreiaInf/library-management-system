package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.dto.RegisterRequest;
import com.rodrigo.library_management_system.entity.User;
import com.rodrigo.library_management_system.enums.Role;
import com.rodrigo.library_management_system.repository.UserRepository;
import com.rodrigo.library_management_system.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthService authService;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder, AuthService authService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authService = authService;
    }

    @PostMapping("/register-client")
    public String registerClient(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.CLIENT);

        userRepository.save(user);

        return "Client registered";
    }

    @PostMapping("/register-owner")
    public String registerOwner(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.OWNER);

        userRepository.save(user);

        return "Owner registered";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return authService.getUsers();
    }

}
