package com.project.locationbiens.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "The first name cannot be empty")
    private String firstName;

    @NotBlank(message = "The last name cannot be empty")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "The email cannot be empty")
    private String email;

    @NotBlank(message = "The password cannot be empty") 
    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String password;

    //@NotBlank(message = "The role cannot be empty")
    //private String role; // USER or ADMIN
}

