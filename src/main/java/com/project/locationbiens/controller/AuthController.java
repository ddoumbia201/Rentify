package com.project.locationbiens.controller;

import com.project.locationbiens.dto.LoginRequest;
import com.project.locationbiens.dto.RegisterRequest;
import com.project.locationbiens.model.User;
import com.project.locationbiens.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository; // Inject the UserRepository to interact with the database
    private final PasswordEncoder passwordEncoder; // Inject the PasswordEncoder to hash passwords

    // Constructor injection for UserRepository and PasswordEncoder
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Check if the email is already registered
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email is already in use"));
        }

        // Create a new User entity and set its properties
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Hash the password
        user.setRole(registerRequest.getRole());

        //
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set the role to "USER" if not provided or blank
        String role = (registerRequest.getRole() != null && !registerRequest.getRole().isBlank())
         ? registerRequest.getRole().toUpperCase() 
         : "USER";

        user.setRole(role);

        // Save the new user to the database
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        
        // Check if the user exists and if the password matches
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = optionalUser.get();

        // Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        // If login is successful, return a success message and user details (excluding password)
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "user", Map.of(
                        "id", user.getId(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        ));
    }
}
