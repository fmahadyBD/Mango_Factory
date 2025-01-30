package com.f.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
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
    //     return adminToUserService.getAllUsers();
        
    // }

    @GetMapping("/get-all")
    public Page<User> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size

    ) {
        return adminToUserService.getUsers(page,size);
        
    }
    

    
    
}
