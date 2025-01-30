package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.Token;
import com.f.backend.entity.User;
import com.f.backend.enums.Role;
import com.f.backend.exceptions.EmailException;
import com.f.backend.jwt.JwtService;
import com.f.backend.repository.TokenRepository;
import com.f.backend.repository.UserRepository;
import com.f.backend.request.LoginForm;
import com.f.backend.request.NewUserRegistrationRequest;
import com.f.backend.response.AuthenticationResponse;

import org.springframework.security.core.AuthenticationException;

import jakarta.mail.MessagingException;

@Service
public class AuthService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            TokenRepository tokenRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    private void checkIfUserExists(NewUserRegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("User with this phone number already exists");
        }

    }

    public AuthenticationResponse register(NewUserRegistrationRequest request, MultipartFile imageFile)
            throws IOException {
        checkIfUserExists(request);

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IOException("Image file is required");
        }

        User user = new User();
        String imageFileName = savedImage(imageFile, request);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());

        // Set Others
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setLock(true);
        user.setActive(false);
        user.setImage(imageFileName);
        userRepository.save(user);

        // sendActivationEmail(user);
        String jwt = jwtService.generateToken(user);
        savedToken(jwt, user);

        return new AuthenticationResponse(jwt, "User registration is successful!");
    }

    private void sendActivationEmail(User user) {
        String activationLink = "http://localhost:8080/active/" + user.getId();
        String mailText = "<h2>Dear " + user.getName() + ",</h2>"
                + "<p>Please click on the following link to confirm your registration:</p>"
                + "<a href=\"" + activationLink + "\">Activate Account</a>";
        String subject = "Confirm Registration";

        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, mailText);

        } catch (MessagingException e) {
            throw new EmailException("Can not sent email properly");
        }

    }

    // public AuthenticationResponse authenticate(LoginForm requet) {


        
    //     authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(requet.getEmail(), requet.getPassword()));
    //     User user = userRepository.findByEmail(requet.getEmail()).orElseThrow();

    //     String jwt = jwtService.generateToken(user);
    //     removedAllTokenByUser(user);

    //     savedToken(jwt, user);
    //     return new AuthenticationResponse(jwt, "User login Successfull");
    // }


        public AuthenticationResponse authenticate(LoginForm request) {


        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }

        try {
            
            // Authenticate the user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Find the user in the repository
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

            // Generate JWT token
            String jwt = jwtService.generateToken(user);

            // Revoke all existing tokens for the user (if applicable)
            removedAllTokenByUser(user);

            // Save the new token
            savedToken(jwt, user);

            // Return the authentication response
            return new AuthenticationResponse(jwt, "User login successful");
        } catch (AuthenticationException e) {
            // Handle authentication failures (e.g., invalid credentials)
            throw new BadCredentialsException("Invalid email or password");
        }
    }


    public String activeUser(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not Found with this ID " + id));

        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            return "User Activated Successfully!";
        } else {
            return "Invalid Activation Token!";
        }

    }

    private String savedImage(MultipartFile file, NewUserRegistrationRequest user) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "users");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = user.getName() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;

    }

    private void savedToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLogout(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private void removedAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokenByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t -> {
            t.setLogout(true);
        });
        tokenRepository.saveAll(validTokens);
    }

}
