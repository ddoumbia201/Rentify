package com.project.locationbiens.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "The start date cannot be empty")
    @Column(nullable = false)
    private String startDate;

    @NotBlank(message = "The end date cannot be empty")
    @Column(nullable = false)
    private String endDate;

    @NotBlank(message = "The total price cannot be empty")
    @Column(nullable = false)
    private String totalPrice;

    @NotBlank(message = "The status cannot be empty")
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "good_id", nullable = false)
    private Good good;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;
}
