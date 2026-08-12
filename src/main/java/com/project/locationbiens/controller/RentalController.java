package com.project.locationbiens.controller;

import com.project.locationbiens.dto.RentalRequest;
import com.project.locationbiens.model.Good;
import com.project.locationbiens.model.Rental;
import com.project.locationbiens.model.User;
import com.project.locationbiens.repository.GoodRepository;
import com.project.locationbiens.repository.RentalRepository;
import com.project.locationbiens.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/rentals") 
public class RentalController {
    
    private final RentalRepository rentalRepository;
    private final GoodRepository goodRepository;
    private final UserRepository userRepository;

    // Constructor injection for the repositories
    public RentalController(RentalRepository rentalRepository, GoodRepository goodRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.goodRepository = goodRepository;
        this.userRepository = userRepository;
    }

    // create a new rental
    @PostMapping
    public ResponseEntity<Rental> createRental(@Valid @RequestBody RentalRequest rentalRequest, Authentication authentication) {
        User renter = userRepository.findByEmail(authentication.getName()).orElseThrow();

        // Fetch the good to be rented
        Good good = goodRepository.findById(rentalRequest.getGoodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found"));

        
        if (rentalRequest.getEndDate().isBefore(rentalRequest.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        // Calculate the total price based on the rental duration and the good's daily price
        long days = ChronoUnit.DAYS.between(rentalRequest.getStartDate(), rentalRequest.getEndDate());
        long billedDays = Math.max(days, 1);

        // Create a new rental entity
        Rental rental = new Rental();
        rental.setRenter(renter);
        rental.setGood(good);
        rental.setStartDate(rentalRequest.getStartDate());
        rental.setEndDate(rentalRequest.getEndDate());
        rental.setTotalPrice(good.getPriceperday().multiply(BigDecimal.valueOf(billedDays)));
        rental.setStatus("EN_ATTENTE"); // Set the initial status of the rental

        // Save the rental to the database
        rentalRepository.save(rental);

        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping("/mine")
    public List<Rental> listMine(Authentication authentication) {
        User renter = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return rentalRepository.findByRenterId(renter.getId());
    }
}
