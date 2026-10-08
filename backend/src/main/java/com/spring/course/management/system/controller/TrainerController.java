package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.TrainerRequest;
import com.spring.course.management.system.dto.TrainerResponse;
import com.spring.course.management.system.service.TrainerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@SecurityRequirement(name = "bearerAuth")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<TrainerResponse> createTrainer(
            @Valid @RequestBody TrainerRequest request) {

        return ResponseEntity
                .status(201)
                .body(trainerService.createTrainer(request));
    }

    @GetMapping
    public ResponseEntity<List<TrainerResponse>> getAllTrainers() {

        return ResponseEntity.ok(
                trainerService.getAllTrainers()
        );
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerResponse> getTrainerById(
            @PathVariable Long trainerId) {

        return ResponseEntity.ok(
                trainerService.getTrainerById(trainerId)
        );
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable Long trainerId,
            @Valid @RequestBody TrainerRequest request) {

        return ResponseEntity.ok(
                trainerService.updateTrainer(trainerId, request)
        );
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<Void> deleteTrainer(
            @PathVariable Long trainerId) {

        trainerService.deleteTrainer(trainerId);

        return ResponseEntity.noContent().build();
    }
}
