package com.rodrigo.library_management_system.service;

import com.rodrigo.library_management_system.entity.User;
import com.rodrigo.library_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
