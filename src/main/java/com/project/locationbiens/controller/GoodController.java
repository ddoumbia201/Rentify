package com.project.locationbiens.controller;

import com.project.locationbiens.model.Good;
import com.project.locationbiens.repository.GoodRepository;
import com.project.locationbiens.dto.GoodRequest;
import com.project.locationbiens.repository.UserRepository;
import com.project.locationbiens.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodController {

    private final GoodRepository goodRepository;
    private final UserRepository userRepository;

    public GoodController(GoodRepository goodRepository, UserRepository userRepository) {
        this.goodRepository = goodRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Good> getAllAvailableGoods() {
        return goodRepository.findByAvailableTrue();
    }

    @GetMapping("/{id}")
    public Good getOne(@PathVariable Long id) {
        return goodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found"));
    }

    // Create a new advertisement (Good) for the authenticated user
    @PostMapping
    public ResponseEntity<Good> create(@Valid @RequestBody GoodRequest request, Authentication authentication) {
        User owner = userRepository.findByEmail(authentication.getName()).orElseThrow(); // Assuming the user is authenticated and exists in the database

        // Create a new Good entity from the request data
        Good good = new Good();
        good.setTitle(request.getTitle());
        good.setDescription(request.getDescription());
        good.setPriceperday(request.getPriceperday());
        good.setCategory(request.getCategory());
        good.setLocation(request.getLocation());
        good.setAvailable(true); // Set the good as available by default
        good.setOwner(owner);

        // Save the new Good entity to the database
        goodRepository.save(good);
        return ResponseEntity.status(HttpStatus.CREATED).body(good);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Good> update(@PathVariable Long id, @Valid @RequestBody GoodRequest request, Authentication authentication) {
        Good good = goodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found"));

        ensureOwner(good, authentication); // Check if the authenticated user is the owner of the good

        // Update the good's details with the request data
        good.setTitle(request.getTitle());
        good.setDescription(request.getDescription());
        good.setPriceperday(request.getPriceperday());
        good.setCategory(request.getCategory());
        good.setLocation(request.getLocation());

        return ResponseEntity.ok(goodRepository.save(good));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        // Find the good by ID, or throw a 404 error if not found
        Good good = goodRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found")); 
        
        ensureOwner(good, authentication); // Check if the authenticated user is the owner of the good

        goodRepository.delete(good);
        return ResponseEntity.ok(Map.of("message", "Good deleted"));
    }

    @GetMapping("/mine")
    public List<Good> listMine(Authentication authentication) {
        User owner = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return goodRepository.findByOwnerId(owner.getId());
    }

    // Helper method to ensure that the authenticated user is the owner of the good
    private void ensureOwner(Good good, Authentication authentication) {
    if (!good.getOwner().getEmail().equals(authentication.getName())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this good");
    }
    }

}
