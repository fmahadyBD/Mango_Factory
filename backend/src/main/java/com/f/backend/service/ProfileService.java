package com.f.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.f.backend.entity.User;
import com.f.backend.repository.UserRepository;
import com.f.backend.request.NewUserRegistrationRequest;
import jakarta.transaction.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found!"));
        return user;
    }

    @Transactional
    public void updateUser(long id, NewUserRegistrationRequest updatedUserRequest) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        if (updatedUserRequest.getName() != null) {
            oldUser.setName(updatedUserRequest.getName());
        }
        if (updatedUserRequest.getEmail() != null) {
            oldUser.setEmail(updatedUserRequest.getEmail());
        }
        if (updatedUserRequest.getPassword() != null) {
            oldUser.setPassword(updatedUserRequest.getPassword());
        }
        if (updatedUserRequest.getDob() != null) {
            oldUser.setDob(updatedUserRequest.getDob());
        }
        if (updatedUserRequest.getAddress() != null) {
            oldUser.setAddress(updatedUserRequest.getAddress());
        }
        if (updatedUserRequest.getPhone() != null) {
            oldUser.setPhone(updatedUserRequest.getPhone());
        }
        if (updatedUserRequest.getGender() != null) {
            oldUser.setGender(updatedUserRequest.getGender());
        }

        userRepository.save(oldUser);
    }

}
