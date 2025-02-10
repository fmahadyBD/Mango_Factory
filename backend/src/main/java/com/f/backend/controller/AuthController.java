package com.f.backend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.request.LoginForm;
import com.f.backend.request.NewUserRegistrationRequest;
import com.f.backend.response.AuthenticationResponse;
import com.f.backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
// @RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginForm loginForm) {
        return ResponseEntity.ok(authService.authenticate(loginForm));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart(value = "user") @Valid NewUserRegistrationRequest newUser,
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println("File is missing!");
            return ResponseEntity.badRequest().body(null);
        } else {
            System.out.println("File received: " + file.getOriginalFilename());
        }

        return ResponseEntity.ok(authService.register(newUser, file));
    }

}
