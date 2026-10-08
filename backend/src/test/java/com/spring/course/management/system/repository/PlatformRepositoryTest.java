package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlatformRepositoryTest {

    @Autowired
    private PlatformRepository platformRepository;


    @Test
    void testSavePlatform() {

        Platform platform = new Platform();

        platform.setPlatformName("Udemy");
        platform.setEmail("udemy@test.com");
        platform.setWebsite("https://www.udemy.com");

        Platform savedPlatform =
                platformRepository.save(platform);

        assertNotNull(savedPlatform.getPlatformId());

        assertEquals(
                "Udemy",
                savedPlatform.getPlatformName()
        );

        assertEquals(
                "udemy@test.com",
                savedPlatform.getEmail()
        );

        assertEquals(
                "https://www.udemy.com",
                savedPlatform.getWebsite()
        );
    }


    @Test
    void testFindAllPlatforms() {

        Platform platform1 = new Platform();

        platform1.setPlatformName("Udemy");
        platform1.setEmail("udemy1@test.com");
        platform1.setWebsite("https://www.udemy.com");


        Platform platform2 = new Platform();

        platform2.setPlatformName("Coursera");
        platform2.setEmail("coursera@test.com");
        platform2.setWebsite("https://www.coursera.org");


        Platform savedPlatform1 =
                platformRepository.save(platform1);

        Platform savedPlatform2 =
                platformRepository.save(platform2);

        List<Platform> platforms =
                platformRepository.findAll();

        assertTrue(platforms.size() >= 2);

        assertTrue(
                platforms.stream()
                        .anyMatch(platform ->
                                platform.getPlatformId()
                                        .equals(savedPlatform1.getPlatformId()))
        );

        assertTrue(
                platforms.stream()
                        .anyMatch(platform ->
                                platform.getPlatformId()
                                        .equals(savedPlatform2.getPlatformId()))
        );
    }


    @Test
    void testFindPlatformById() {

        Platform platform = new Platform();

        platform.setPlatformName("Udemy");
        platform.setEmail("find@test.com");
        platform.setWebsite("https://www.udemy.com");

        Platform savedPlatform =
                platformRepository.save(platform);

        Optional<Platform> result =
                platformRepository.findById(
                        savedPlatform.getPlatformId()
                );

        assertTrue(result.isPresent());

        assertEquals(
                "Udemy",
                result.get().getPlatformName()
        );

        assertEquals(
                "find@test.com",
                result.get().getEmail()
        );

        assertEquals(
                "https://www.udemy.com",
                result.get().getWebsite()
        );
    }


    @Test
    void testFindPlatformByIdNotFound() {

        Optional<Platform> result =
                platformRepository.findById(99999L);

        assertFalse(result.isPresent());
    }


    @Test
    void testUpdatePlatform() {

        Platform platform = new Platform();

        platform.setPlatformName("Udemy");
        platform.setEmail("update@test.com");
        platform.setWebsite("https://www.udemy.com");

        Platform savedPlatform =
                platformRepository.save(platform);

        savedPlatform.setPlatformName("Udemy Updated");
        savedPlatform.setWebsite("https://updated.udemy.com");

        Platform updatedPlatform =
                platformRepository.save(savedPlatform);

        assertEquals(
                "Udemy Updated",
                updatedPlatform.getPlatformName()
        );

        assertEquals(
                "https://updated.udemy.com",
                updatedPlatform.getWebsite()
        );
    }


    @Test
    void testDeletePlatform() {

        Platform platform = new Platform();

        platform.setPlatformName("Delete Platform");
        platform.setEmail("delete@test.com");
        platform.setWebsite("https://delete.com");

        Platform savedPlatform =
                platformRepository.save(platform);

        Long platformId =
                savedPlatform.getPlatformId();

        platformRepository.delete(savedPlatform);

        Optional<Platform> result =
                platformRepository.findById(platformId);

        assertFalse(result.isPresent());
    }
}