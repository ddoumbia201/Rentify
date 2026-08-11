package com.project.locationbiens.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Goods")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Good {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "The title cannot be empty")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "The description cannot be empty")
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "The price per day must be greater than 0")
    private BigDecimal priceperday;

    @NotBlank(message = "The category cannot be empty")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "The location cannot be empty")
    @Column(nullable = false)
    private String location;

    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY) // fetch type set to LAZY to avoid loading the owner unless needed
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
