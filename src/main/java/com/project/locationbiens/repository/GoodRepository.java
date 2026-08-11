package com.project.locationbiens.repository;

import com.project.locationbiens.model.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoodRepository extends JpaRepository<Good, Long> {
    // searches for all goods that are available for rent
    List<Good> findByAvailableTrue();
    // searches for all goods that belong to a specific owner
    List<Good> findByOwnerId(Long ownerId);
}
