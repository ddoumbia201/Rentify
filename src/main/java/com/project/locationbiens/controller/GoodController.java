package com.project.locationbiens.controller;

import com.project.locationbiens.model.Good;
import com.project.locationbiens.repository.GoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodController {

    private final GoodRepository goodRepository;

    public GoodController(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
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
}
