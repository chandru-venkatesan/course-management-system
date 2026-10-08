package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.TrainerRequest;
import com.spring.course.management.system.dto.TrainerResponse;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.TrainerService;

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

@WebMvcTest(TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * We create ObjectMapper manually.
     *
     * This avoids the ObjectMapper bean problem
     * that occurred in StudentControllerTest.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock TrainerService.
     *
     * The controller will use this mock instead
     * of the real TrainerService.
     */
    @MockitoBean
    private TrainerService trainerService;

    /*
     * Mock JWT filter so security does not interfere
     * with this controller test.
     */
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * Mock RateLimitService for the same reason.
     */
    @MockitoBean
    private RateLimitService rateLimitService;


    // =========================================================
    // 1. CREATE TRAINER
    // =========================================================

    @Test
    void testCreateTrainer() throws Exception {

        TrainerRequest request =
                new TrainerRequest();

        request.setTrainerName("Arun Kumar");
        request.setEmail("arun.trainer@gmail.com");
        request.setSpecialization("Java");
        request.setExperience(5);


        TrainerResponse response =
                new TrainerResponse();

        response.setTrainerId(1L);
        response.setTrainerName("Arun Kumar");
        response.setEmail("arun.trainer@gmail.com");
        response.setSpecialization("Java");
        response.setExperience(5);


        when(trainerService.createTrainer(
                any(TrainerRequest.class)
        )).thenReturn(response);


        mockMvc.perform(
                        post("/api/trainers")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.trainerId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.trainerName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arun.trainer@gmail.com")
                )
                .andExpect(
                        jsonPath("$.specialization")
                                .value("Java")
                )
                .andExpect(
                        jsonPath("$.experience")
                                .value(5)
                );
    }


    // =========================================================
    // 2. GET ALL TRAINERS
    // =========================================================

    @Test
    void testGetAllTrainers() throws Exception {

        TrainerResponse trainer1 =
                new TrainerResponse();

        trainer1.setTrainerId(1L);
        trainer1.setTrainerName("Arun Kumar");
        trainer1.setEmail("arun.trainer@gmail.com");
        trainer1.setSpecialization("Java");
        trainer1.setExperience(5);


        TrainerResponse trainer2 =
                new TrainerResponse();

        trainer2.setTrainerId(2L);
        trainer2.setTrainerName("Priya Sharma");
        trainer2.setEmail("priya.trainer@gmail.com");
        trainer2.setSpecialization("Spring Boot");
        trainer2.setExperience(7);


        when(trainerService.getAllTrainers())
                .thenReturn(
                        List.of(
                                trainer1,
                                trainer2
                        )
                );


        mockMvc.perform(
                        get("/api/trainers")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].trainerId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].trainerName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$[0].email")
                                .value("arun.trainer@gmail.com")
                )
                .andExpect(
                        jsonPath("$[1].trainerId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].trainerName")
                                .value("Priya Sharma")
                )
                .andExpect(
                        jsonPath("$[1].specialization")
                                .value("Spring Boot")
                );
    }


    // =========================================================
    // 3. GET TRAINER BY ID
    // =========================================================

    @Test
    void testGetTrainerById() throws Exception {

        TrainerResponse response =
                new TrainerResponse();

        response.setTrainerId(1L);
        response.setTrainerName("Arun Kumar");
        response.setEmail("arun.trainer@gmail.com");
        response.setSpecialization("Java");
        response.setExperience(5);


        when(trainerService.getTrainerById(1L))
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/trainers/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.trainerId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.trainerName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arun.trainer@gmail.com")
                )
                .andExpect(
                        jsonPath("$.specialization")
                                .value("Java")
                )
                .andExpect(
                        jsonPath("$.experience")
                                .value(5)
                );
    }


    // =========================================================
    // 4. UPDATE TRAINER
    // =========================================================

    @Test
    void testUpdateTrainer() throws Exception {

        TrainerRequest request =
                new TrainerRequest();

        request.setTrainerName("Arun Kumar Updated");
        request.setEmail("arun.updated@gmail.com");
        request.setSpecialization("Advanced Java");
        request.setExperience(6);


        TrainerResponse response =
                new TrainerResponse();

        response.setTrainerId(1L);
        response.setTrainerName("Arun Kumar Updated");
        response.setEmail("arun.updated@gmail.com");
        response.setSpecialization("Advanced Java");
        response.setExperience(6);


        when(
                trainerService.updateTrainer(
                        eq(1L),
                        any(TrainerRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        put("/api/trainers/1")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.trainerId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.trainerName")
                                .value("Arun Kumar Updated")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arun.updated@gmail.com")
                )
                .andExpect(
                        jsonPath("$.specialization")
                                .value("Advanced Java")
                )
                .andExpect(
                        jsonPath("$.experience")
                                .value(6)
                );
    }


    // =========================================================
    // 5. DELETE TRAINER
    // =========================================================

    @Test
    void testDeleteTrainer() throws Exception {

        /*
         * deleteTrainer() returns void.
         *
         * Therefore we use doNothing().
         */
        doNothing()
                .when(trainerService)
                .deleteTrainer(1L);


        mockMvc.perform(
                        delete("/api/trainers/1")
                )
                .andExpect(status().isNoContent());
    }
}