package com.f.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.f.backend.entity.User;
import com.f.backend.service.AdminToUserService;

@RestController
@RequestMapping("/admin/user")
public class AdminToUserController {

    @Autowired
    private AdminToUserService adminToUserService;

    // @GetMapping("/get-all")
    // public List<User> getAllUsers() {
    // return adminToUserService.getAllUsers();

    // }

    @GetMapping("/get-all")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        return adminToUserService.getUsers(page, size);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable long id) {

        try {
            adminToUserService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Delete User by this id: " + id);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, String>> updateTheRole(
            @PathVariable long id,
            @RequestParam String role) { // Use @RequestParam for simple query parameters
        Map<String, String> response = new HashMap<>();
    
        try {
            adminToUserService.changeRole(id, role);
            response.put("message", "Role updated successfully");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            response.put("error", "User not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return 404 for user not found
        } catch (IllegalArgumentException e) {
            response.put("error", "Invalid role: " + role);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Return 400 for invalid role
        } catch (Exception e) {
            response.put("error", "Failed to update role: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return 500 for other errors
        }
    }


}
