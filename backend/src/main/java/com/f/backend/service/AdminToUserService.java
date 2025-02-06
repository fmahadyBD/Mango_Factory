package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.f.backend.entity.Token;
import com.f.backend.entity.User;
import com.f.backend.repository.TokenRepository;
import com.f.backend.repository.UserRepository;
import com.f.backend.enums.Role;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminToUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found the user by this ID: " + id));

        // Explicitly remove all tokens before deleting user
        removeAllTokenByUser(user);

        // Ensure tokens are actually deleted
        tokenRepository.flush();

        // Now proceed with deleting the user
        deleteUserImage(user.getImage());
        userRepository.delete(user); // Ensure this happens after tokens are deleted
    }

    private void deleteUserImage(String imageFileName) {
        if (imageFileName == null || imageFileName.isEmpty()) {
            return;
        }
        try {
            Path imagePath = Paths.get(uploadDir, "users", imageFileName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete user image");
        }
    }

    @Transactional
    public void removeAllTokenByUser(User user) {
        tokenRepository.deleteAllByUser_Id(user.getId());
    }

    public void changeRole(long id, String roleName) {
        Role role = Role.valueOf(roleName.toUpperCase());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by this id"));
        user.setRole(role);


    }

}
