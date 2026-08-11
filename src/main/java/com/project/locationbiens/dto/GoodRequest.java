package com.project.locationbiens.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GoodRequest {

    @NotBlank(message = "The title cannot be empty")
    private String title;

    @NotBlank(message = "The description cannot be empty")
    private String description;

    @NotNull(message = "The price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be positive")
    private BigDecimal priceperday;

    @NotBlank(message = "The category cannot be empty")
    private String category;

    @NotBlank(message = "The location cannot be empty")
    private String location;
}
