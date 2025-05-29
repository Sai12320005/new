package com.fts.e_commerce.controller;

import com.fts.e_commerce.entity.UserEntity;
import com.fts.e_commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    // Get all users
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    // Create user via /api/users (generic POST)
    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }

    // Register user via /api/users/register
    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
