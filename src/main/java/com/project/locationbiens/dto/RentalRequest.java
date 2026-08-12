package com.project.locationbiens.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RentalRequest {
    
    @NotNull(message = "Good id is required")
    private Long goodId;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future") // ensures that the start date is in the future
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future") // ensures that the end date is in the future
    private LocalDate endDate;
}
