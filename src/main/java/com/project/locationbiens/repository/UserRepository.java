package com.project.locationbiens.repository;

import com.project.locationbiens.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // searches for a user whose email field matches the value passed as a parameter
    Optional<User> findByEmail(String email);

    // checks if a user with the given email exists in the database
    boolean existsByEmail(String email);
}
