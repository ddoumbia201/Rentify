package com.project.locationbiens.repository;

import com.project.locationbiens.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByRenterId(Long renterId); // Custom query method to find rentals by renter ID
}
