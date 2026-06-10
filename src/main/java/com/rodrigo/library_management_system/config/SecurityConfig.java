package com.rodrigo.library_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.GET, "/books/**").permitAll()

                        // CLIENT ACTIONS
                        .requestMatchers(HttpMethod.POST, "/books/*/borrow").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/books/*/return").hasRole("CLIENT")

                        // OWNER MANAGEMENT
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/history/**").hasRole("OWNER")

                        // fallback
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
