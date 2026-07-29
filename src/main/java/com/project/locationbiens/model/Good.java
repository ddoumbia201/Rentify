package com.project.locationbiens.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "The price per day cannot be empty")
    @Column(nullable = false)
    private Double priceperday;

    @NotBlank(message = "The category cannot be empty")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "The location cannot be empty")
    @Column(nullable = false)
    private String location;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}

