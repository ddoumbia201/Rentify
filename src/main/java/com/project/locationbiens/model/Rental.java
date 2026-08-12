package com.project.locationbiens.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The start date cannot be empty")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "The end date cannot be empty")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "The total price cannot be empty")
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "The total price must be greater than 0")
    private BigDecimal totalPrice;

    @NotBlank(message = "The status cannot be empty")
    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_id", nullable = false)
    private Good good;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;
}
