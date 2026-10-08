package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.PlatformRequest;
import com.spring.course.management.system.dto.PlatformResponse;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.PlatformService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlatformController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlatformControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Create ObjectMapper manually.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock PlatformService.
     */
    @MockitoBean
    private PlatformService platformService;

    /*
     * Mock JWT filter.
     */
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * Mock RateLimitService.
     */
    @MockitoBean
    private RateLimitService rateLimitService;


    // =========================================================
    // 1. CREATE PLATFORM
    // =========================================================

    @Test
    void testCreatePlatform() throws Exception {

        PlatformRequest request =
                new PlatformRequest();

        request.setPlatformName("Udemy");
        request.setEmail("support@udemy.com");
        request.setWebsite("https://www.udemy.com");


        PlatformResponse response =
                new PlatformResponse();

        response.setPlatformId(1L);
        response.setPlatformName("Udemy");
        response.setEmail("support@udemy.com");
        response.setWebsite("https://www.udemy.com");


        when(
                platformService.createPlatform(
                        any(PlatformRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/platforms")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.platformId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.platformName")
                                .value("Udemy")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("support@udemy.com")
                )
                .andExpect(
                        jsonPath("$.website")
                                .value("https://www.udemy.com")
                );
    }


    // =========================================================
    // 2. GET ALL PLATFORMS
    // =========================================================

    @Test
    void testGetAllPlatforms() throws Exception {

        PlatformResponse platform1 =
                new PlatformResponse();

        platform1.setPlatformId(1L);
        platform1.setPlatformName("Udemy");
        platform1.setEmail("support@udemy.com");
        platform1.setWebsite("https://www.udemy.com");


        PlatformResponse platform2 =
                new PlatformResponse();

        platform2.setPlatformId(2L);
        platform2.setPlatformName("Coursera");
        platform2.setEmail("support@coursera.org");
        platform2.setWebsite("https://www.coursera.org");


        when(
                platformService.getAllPlatforms()
        ).thenReturn(
                List.of(
                        platform1,
                        platform2
                )
        );


        mockMvc.perform(
                        get("/api/platforms")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].platformId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].platformName")
                                .value("Udemy")
                )
                .andExpect(
                        jsonPath("$[0].email")
                                .value("support@udemy.com")
                )
                .andExpect(
                        jsonPath("$[1].platformId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].platformName")
                                .value("Coursera")
                )
                .andExpect(
                        jsonPath("$[1].website")
                                .value("https://www.coursera.org")
                );
    }


    // =========================================================
    // 3. GET PLATFORM BY ID
    // =========================================================

    @Test
    void testGetPlatformById() throws Exception {

        PlatformResponse response =
                new PlatformResponse();

        response.setPlatformId(1L);
        response.setPlatformName("Udemy");
        response.setEmail("support@udemy.com");
        response.setWebsite("https://www.udemy.com");


        when(
                platformService.getPlatformById(1L)
        ).thenReturn(response);


        mockMvc.perform(
                        get("/api/platforms/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.platformId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.platformName")
                                .value("Udemy")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("support@udemy.com")
                )
                .andExpect(
                        jsonPath("$.website")
                                .value("https://www.udemy.com")
                );
    }


    // =========================================================
    // 4. UPDATE PLATFORM
    // =========================================================

    @Test
    void testUpdatePlatform() throws Exception {

        PlatformRequest request =
                new PlatformRequest();

        request.setPlatformName("Udemy Updated");
        request.setEmail("new-support@udemy.com");
        request.setWebsite("https://www.udemy.com");


        PlatformResponse response =
                new PlatformResponse();

        response.setPlatformId(1L);
        response.setPlatformName("Udemy Updated");
        response.setEmail("new-support@udemy.com");
        response.setWebsite("https://www.udemy.com");


        when(
                platformService.updatePlatform(
                        eq(1L),
                        any(PlatformRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        put("/api/platforms/1")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.platformId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.platformName")
                                .value("Udemy Updated")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("new-support@udemy.com")
                )
                .andExpect(
                        jsonPath("$.website")
                                .value("https://www.udemy.com")
                );
    }


    // =========================================================
    // 5. DELETE PLATFORM
    // =========================================================

    @Test
    void testDeletePlatform() throws Exception {

        /*
         * deletePlatform() returns void,
         * so we use doNothing().
         */
        doNothing()
                .when(platformService)
                .deletePlatform(1L);


        mockMvc.perform(
                        delete("/api/platforms/1")
                )
                .andExpect(status().isNoContent());
    }
}