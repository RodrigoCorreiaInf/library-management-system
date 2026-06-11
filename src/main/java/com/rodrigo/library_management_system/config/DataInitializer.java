package com.rodrigo.library_management_system.config;

import com.rodrigo.library_management_system.entity.User;
import com.rodrigo.library_management_system.enums.Role;
import com.rodrigo.library_management_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByName("owner").isEmpty()) {
            User owner = new User();
            owner.setName("owner");
            owner.setPassword(passwordEncoder.encode("owner123")); // Dynamically hashes perfectly
            owner.setRole(Role.OWNER);
            userRepository.save(owner);
            System.out.println("Generated Owner: owner / owner123");
        }

        if (userRepository.findByName("client").isEmpty()) {
            User client = new User();
            client.setName("client");
            client.setPassword(passwordEncoder.encode("client123")); // Dynamically hashes perfectly
            client.setRole(Role.CLIENT);
            userRepository.save(client);
            System.out.println("Generated Client: client / client123");
        }
    }

}
