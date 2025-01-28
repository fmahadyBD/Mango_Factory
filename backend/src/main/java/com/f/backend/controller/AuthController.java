package com.f.backend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.request.LoginForm;
import com.f.backend.request.NewUserRegistrationRequest;
import com.f.backend.response.AuthenticationResponse;
import com.f.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart(value = "new-user") @Valid NewUserRegistrationRequest newUser,
            @RequestPart(value = "image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(authService.register(newUser, file));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginForm loginForm) {
        return ResponseEntity.ok(authService.authenticate(loginForm));
    }
}
