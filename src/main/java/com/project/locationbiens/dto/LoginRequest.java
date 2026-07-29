package com.project.locationbiens.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "The email cannot be empty")
    private String email;

    @NotBlank(message = "The password cannot be empty")
    private String password;
}
