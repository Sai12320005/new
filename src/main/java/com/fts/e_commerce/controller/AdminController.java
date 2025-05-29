package com.fts.e_commerce.controller;

import com.fts.e_commerce.entity.AdminEntity;
import com.fts.e_commerce.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    private final AdminService adminService;

    // Constructor injection
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminEntity admin) {
        AdminEntity foundAdmin = adminService.getAdminByEmail(admin.getEmail());

        if (foundAdmin == null || !foundAdmin.getPassword().equals(admin.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful.");
        response.put("email", foundAdmin.getEmail());
        return ResponseEntity.ok(response);
    }
}
