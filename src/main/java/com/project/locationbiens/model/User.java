package com.project.locationbiens.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "The email cannot be empty")
    @Email(message = "The email format is invalid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Size(min = 6, message = "The password must contain at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "The role is required")
    @Column(nullable = false)
    private String role; // Exemple : "USER" ou "ADMIN"
}