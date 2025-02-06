package com.f.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.backend.entity.User;
import com.f.backend.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, User>> getUser(@PathVariable long id) {
        Map<String, User> response = new HashMap<>();

        try {
            User user = profileService.getUser(id);
            response.put("data", user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("Not found!  Error" + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }
}
