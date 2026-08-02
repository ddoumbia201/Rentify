package com.project.locationbiens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        // Return a simple message for the admin dashboard
        return ResponseEntity.ok(Map.of(
            "message", "Welcome to the Admin Dashboard!",
            "status", "access granted for ADMIN role"));
    }
}
