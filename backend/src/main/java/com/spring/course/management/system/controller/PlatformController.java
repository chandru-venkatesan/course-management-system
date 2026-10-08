package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.PlatformRequest;
import com.spring.course.management.system.dto.PlatformResponse;
import com.spring.course.management.system.service.PlatformService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
@SecurityRequirement(name = "bearerAuth")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(
            PlatformService platformService) {

        this.platformService = platformService;
    }

    @PostMapping
    public ResponseEntity<PlatformResponse> createPlatform(
            @Valid @RequestBody PlatformRequest request) {

        return ResponseEntity
                .status(201)
                .body(platformService.createPlatform(request));
    }

    @GetMapping
    public ResponseEntity<List<PlatformResponse>> getAllPlatforms() {

        return ResponseEntity.ok(
                platformService.getAllPlatforms()
        );
    }

    @GetMapping("/{platformId}")
    public ResponseEntity<PlatformResponse> getPlatformById(
            @PathVariable Long platformId) {

        return ResponseEntity.ok(
                platformService.getPlatformById(platformId)
        );
    }

    @PutMapping("/{platformId}")
    public ResponseEntity<PlatformResponse> updatePlatform(
            @PathVariable Long platformId,
            @Valid @RequestBody PlatformRequest request) {

        return ResponseEntity.ok(
                platformService.updatePlatform(
                        platformId,
                        request
                )
        );
    }

    @DeleteMapping("/{platformId}")
    public ResponseEntity<Void> deletePlatform(
            @PathVariable Long platformId) {

        platformService.deletePlatform(platformId);

        return ResponseEntity.noContent().build();
    }
}
