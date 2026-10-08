package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.EnrollmentRequest;
import com.spring.course.management.system.dto.EnrollmentResponse;
import com.spring.course.management.system.service.EnrollmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(
            EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {

        return ResponseEntity
                .status(201)
                .body(enrollmentService.createEnrollment(request));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {

        return ResponseEntity.ok(
                enrollmentService.getAllEnrollments()
        );
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(
            @PathVariable Long enrollmentId) {

        return ResponseEntity.ok(
                enrollmentService.getEnrollmentById(enrollmentId)
        );
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);

        return ResponseEntity.noContent().build();
    }
}
