package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.PlatformRequest;
import com.spring.course.management.system.dto.PlatformResponse;
import com.spring.course.management.system.exception.PlatformNotFoundException;
import com.spring.course.management.system.model.Platform;
import com.spring.course.management.system.repository.PlatformRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public PlatformResponse createPlatform(
            PlatformRequest request) {

        Platform platform = new Platform();

        platform.setPlatformName(request.getPlatformName());
        platform.setEmail(request.getEmail());
        platform.setWebsite(request.getWebsite());

        Platform savedPlatform =
                platformRepository.save(platform);

        return mapToResponse(savedPlatform);
    }

    public List<PlatformResponse> getAllPlatforms() {

        return platformRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PlatformResponse getPlatformById(Long platformId) {

        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() ->
                        new PlatformNotFoundException(
                                "Platform not found with ID: "
                                        + platformId
                        ));

        return mapToResponse(platform);
    }

    public PlatformResponse updatePlatform(
            Long platformId,
            PlatformRequest request) {

        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() ->
                        new PlatformNotFoundException(
                                "Platform not found with ID: "
                                        + platformId
                        ));

        platform.setPlatformName(request.getPlatformName());
        platform.setEmail(request.getEmail());
        platform.setWebsite(request.getWebsite());

        Platform updatedPlatform =
                platformRepository.save(platform);

        return mapToResponse(updatedPlatform);
    }

    public void deletePlatform(Long platformId) {

        Platform platform = platformRepository.findById(platformId)
                .orElseThrow(() ->
                        new PlatformNotFoundException(
                                "Platform not found with ID: "
                                        + platformId
                        ));

        platformRepository.delete(platform);
    }

    private PlatformResponse mapToResponse(
            Platform platform) {

        return new PlatformResponse(
                platform.getPlatformId(),
                platform.getPlatformName(),
                platform.getEmail(),
                platform.getWebsite()
        );
    }
}
