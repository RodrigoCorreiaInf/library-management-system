package com.rodrigo.library_management_system.controller;

import com.rodrigo.library_management_system.dto.AuthResponse;
import com.rodrigo.library_management_system.dto.LoginRequest;
import com.rodrigo.library_management_system.dto.RegisterRequest;
import com.rodrigo.library_management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(
//            @RequestBody RegisterRequest request) {
//
//        return ResponseEntity.ok(authService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(
//            @RequestBody LoginRequest request) {
//
//        return ResponseEntity.ok(authService.login(request));
//    }

}
