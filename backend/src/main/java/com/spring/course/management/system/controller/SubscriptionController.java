package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.SubscriptionRequest;
import com.spring.course.management.system.dto.SubscriptionResponse;
import com.spring.course.management.system.service.SubscriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {

        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {

        return ResponseEntity
                .status(201)
                .body(subscriptionService.createSubscription(request));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>>
    getAllSubscriptions() {

        return ResponseEntity.ok(
                subscriptionService.getAllSubscriptions()
        );
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse>
    getSubscriptionById(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionById(subscriptionId)
        );
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long subscriptionId) {

        subscriptionService.deleteSubscription(subscriptionId);

        return ResponseEntity.noContent().build();
    }
}
