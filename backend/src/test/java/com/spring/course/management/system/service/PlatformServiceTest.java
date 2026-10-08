package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.PlatformRequest;
import com.spring.course.management.system.dto.PlatformResponse;
import com.spring.course.management.system.exception.PlatformNotFoundException;
import com.spring.course.management.system.model.Platform;
import com.spring.course.management.system.repository.PlatformRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;


    // ---------------------------------------------------------
    // 1. Test Get All Platforms
    // ---------------------------------------------------------

    @Test
    void testGetAllPlatforms() {

        Platform platform1 = new Platform();

        platform1.setPlatformId(1L);
        platform1.setPlatformName("Udemy");
        platform1.setEmail("support@udemy.com");
        platform1.setWebsite("https://www.udemy.com");


        Platform platform2 = new Platform();

        platform2.setPlatformId(2L);
        platform2.setPlatformName("Coursera");
        platform2.setEmail("support@coursera.org");
        platform2.setWebsite("https://www.coursera.org");


        when(platformRepository.findAll())
                .thenReturn(List.of(platform1, platform2));


        List<PlatformResponse> result =
                platformService.getAllPlatforms();


        assertEquals(2, result.size());

        assertEquals(
                1L,
                result.get(0).getPlatformId()
        );

        assertEquals(
                "Udemy",
                result.get(0).getPlatformName()
        );

        assertEquals(
                "Coursera",
                result.get(1).getPlatformName()
        );


        verify(platformRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 2. Test Get Platform By ID
    // ---------------------------------------------------------

    @Test
    void testGetPlatformById() {

        Platform platform = new Platform();

        platform.setPlatformId(1L);
        platform.setPlatformName("Udemy");
        platform.setEmail("support@udemy.com");
        platform.setWebsite("https://www.udemy.com");


        when(platformRepository.findById(1L))
                .thenReturn(Optional.of(platform));


        PlatformResponse result =
                platformService.getPlatformById(1L);


        assertEquals(
                1L,
                result.getPlatformId()
        );

        assertEquals(
                "Udemy",
                result.getPlatformName()
        );

        assertEquals(
                "support@udemy.com",
                result.getEmail()
        );

        assertEquals(
                "https://www.udemy.com",
                result.getWebsite()
        );


        verify(platformRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 3. Test Get Platform By ID - Not Found
    // ---------------------------------------------------------

    @Test
    void testGetPlatformByIdPlatformNotFound() {

        when(platformRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.getPlatformById(999L)
        );


        verify(platformRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 4. Test Create Platform
    // ---------------------------------------------------------

    @Test
    void testCreatePlatform() {

        PlatformRequest request = new PlatformRequest();

        request.setPlatformName("Udemy");
        request.setEmail("support@udemy.com");
        request.setWebsite("https://www.udemy.com");


        Platform savedPlatform = new Platform();

        savedPlatform.setPlatformId(1L);
        savedPlatform.setPlatformName("Udemy");
        savedPlatform.setEmail("support@udemy.com");
        savedPlatform.setWebsite("https://www.udemy.com");


        when(platformRepository.save(any(Platform.class)))
                .thenReturn(savedPlatform);


        PlatformResponse result =
                platformService.createPlatform(request);


        assertEquals(
                1L,
                result.getPlatformId()
        );

        assertEquals(
                "Udemy",
                result.getPlatformName()
        );

        assertEquals(
                "support@udemy.com",
                result.getEmail()
        );

        assertEquals(
                "https://www.udemy.com",
                result.getWebsite()
        );


        verify(platformRepository, times(1))
                .save(any(Platform.class));
    }


    // ---------------------------------------------------------
    // 5. Test Update Platform
    // ---------------------------------------------------------

    @Test
    void testUpdatePlatform() {

        PlatformRequest request = new PlatformRequest();

        request.setPlatformName("Udemy Updated");
        request.setEmail("new@udemy.com");
        request.setWebsite("https://updated.udemy.com");


        Platform existingPlatform = new Platform();

        existingPlatform.setPlatformId(1L);
        existingPlatform.setPlatformName("Udemy");
        existingPlatform.setEmail("support@udemy.com");
        existingPlatform.setWebsite("https://www.udemy.com");


        when(platformRepository.findById(1L))
                .thenReturn(Optional.of(existingPlatform));

        when(platformRepository.save(any(Platform.class)))
                .thenReturn(existingPlatform);


        PlatformResponse result =
                platformService.updatePlatform(
                        1L,
                        request
                );


        assertEquals(
                1L,
                result.getPlatformId()
        );

        assertEquals(
                "Udemy Updated",
                result.getPlatformName()
        );

        assertEquals(
                "new@udemy.com",
                result.getEmail()
        );

        assertEquals(
                "https://updated.udemy.com",
                result.getWebsite()
        );


        verify(platformRepository, times(1))
                .findById(1L);

        verify(platformRepository, times(1))
                .save(existingPlatform);
    }


    // ---------------------------------------------------------
    // 6. Test Update Platform - Not Found
    // ---------------------------------------------------------

    @Test
    void testUpdatePlatformPlatformNotFound() {

        PlatformRequest request = new PlatformRequest();

        request.setPlatformName("Udemy");
        request.setEmail("support@udemy.com");
        request.setWebsite("https://www.udemy.com");


        when(platformRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.updatePlatform(
                        999L,
                        request
                )
        );


        verify(platformRepository, times(1))
                .findById(999L);

        verify(platformRepository, never())
                .save(any(Platform.class));
    }


    // ---------------------------------------------------------
    // 7. Test Delete Platform
    // ---------------------------------------------------------

    @Test
    void testDeletePlatform() {

        Platform platform = new Platform();

        platform.setPlatformId(1L);
        platform.setPlatformName("Udemy");
        platform.setEmail("support@udemy.com");
        platform.setWebsite("https://www.udemy.com");


        when(platformRepository.findById(1L))
                .thenReturn(Optional.of(platform));


        doNothing()
                .when(platformRepository)
                .delete(platform);


        platformService.deletePlatform(1L);


        verify(platformRepository, times(1))
                .findById(1L);

        verify(platformRepository, times(1))
                .delete(platform);
    }


    // ---------------------------------------------------------
    // 8. Test Delete Platform - Not Found
    // ---------------------------------------------------------

    @Test
    void testDeletePlatformPlatformNotFound() {

        when(platformRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.deletePlatform(999L)
        );


        verify(platformRepository, times(1))
                .findById(999L);

        verify(platformRepository, never())
                .delete(any(Platform.class));
    }
}