package com.project.locationbiens.controller;

import com.project.locationbiens.model.Good;
import com.project.locationbiens.model.User;
import com.project.locationbiens.repository.GoodRepository;
import com.project.locationbiens.repository.RentalRepository;
import com.project.locationbiens.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final RentalRepository rentalRepository;

    public AdminController(UserRepository userRepository, GoodRepository goodRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.goodRepository = goodRepository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping("/users") // endpoint to get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @GetMapping("/goods")
    public List<Good> getAllGoods() {
        return goodRepository.findAll();
    }

    @DeleteMapping("/goods/{id}")
    public ResponseEntity<?> deleteGood(@PathVariable Long id) {
        Good good = goodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found"));
        
        boolean hasRentals = !rentalRepository.findByGoodId(id).isEmpty();
        if (hasRentals) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete a good that has associated rentals");
        }
        goodRepository.delete(good);
        return ResponseEntity.ok(Map.of("message", "Good deleted"));
    }
}
