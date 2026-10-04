package com.gymmanagement.controller;

import com.gymmanagement.dto.request.TrainerRequest;
import com.gymmanagement.dto.response.TrainerResponse;
import com.gymmanagement.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public ResponseEntity<List<TrainerResponse>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TrainerResponse>> getActiveTrainers() {
        return ResponseEntity.ok(trainerService.getActiveTrainers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getTrainerById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponse> createTrainer(@Valid @RequestBody TrainerRequest request) {
        TrainerResponse response = trainerService.createTrainer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponse> updateTrainer(@PathVariable Long id, @Valid @RequestBody TrainerRequest request) {
        return ResponseEntity.ok(trainerService.updateTrainer(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }
}
