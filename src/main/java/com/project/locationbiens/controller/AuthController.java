package com.project.locationbiens.controller;

import com.project.locationbiens.dto.LoginRequest;
import com.project.locationbiens.dto.RegisterRequest;
import com.project.locationbiens.model.User;
import com.project.locationbiens.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository; // Inject the UserRepository to interact with the database
    private final PasswordEncoder passwordEncoder; // Inject the PasswordEncoder to hash passwords

    private final AuthenticationManager authenticationManager; // Inject the AuthenticationManager for authentication

    // Constructor injection for UserRepository and PasswordEncoder
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager; // Initialize the AuthenticationManager
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
        //user.setRole(registerRequest.getRole());

        //
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set the role to "USER" if not provided or blank
        //String role = (registerRequest.getRole() != null && !registerRequest.getRole().isBlank())
        // ? registerRequest.getRole().toUpperCase() 
        // : "USER";

        user.setRole("USER"); // Default role is USER

        // Save the new user to the database
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );
        //Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        // Check if the user exists and if the password matches
        //if (optionalUser.isEmpty()) {
        //    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        //            .body(Map.of("error", "Invalid email or password"));
        //}
        //User user = optionalUser.get();

        SecurityContext context = SecurityContextHolder.createEmptyContext(); // Create a new SecurityContext
        context.setAuthentication(authentication); 
        SecurityContextHolder.setContext(context); 
        request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", context); // Store the SecurityContext in the session

        // Check if the provided password matches the stored hashed password
        //if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        //    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        //            .body(Map.of("error", "Invalid email or password"));
        //}

        // Retrieve the user details from the database
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        // Invalidate the session to log out the user
        request.getSession().invalidate();
        SecurityContextHolder.clearContext(); // Clear the SecurityContext to remove authentication information
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        // Check if the user is authenticated
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User is not authenticated"));
        }

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        // Return the user details (excluding password)
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }
}
