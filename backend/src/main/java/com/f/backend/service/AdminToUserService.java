package com.f.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found the user by this ID: " + id));
        deleteUserImage(user.getImage());
        removeAllTokenByUser(user);
        userRepository.delete(user);
        return;
    }

    private void deleteUserImage(String imageFileName) {
        if (imageFileName == null || imageFileName.isEmpty()) {
            return;
        }
        try {
            Path imagePath = Paths.get(uploadDir, imageFileName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete user image");
        }
    }

    public void removeAllTokenByUser(User user){
        List<Token> tokens= tokenRepository.findAllTokenByUser(user.getId());
        if(tokens.isEmpty()) return;

        tokenRepository.deleteAll(tokens);;
    }

    
}
