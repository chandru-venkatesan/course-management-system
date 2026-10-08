package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.EnrollmentRequest;
import com.spring.course.management.system.dto.EnrollmentResponse;
import com.spring.course.management.system.model.EnrollmentStatus;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.EnrollmentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Create ObjectMapper manually.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock EnrollmentService.
     */
    @MockitoBean
    private EnrollmentService enrollmentService;

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
    // 1. CREATE ENROLLMENT
    // =========================================================

    @Test
    void testCreateEnrollment() throws Exception {

        EnrollmentRequest request =
                new EnrollmentRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        EnrollmentResponse response =
                new EnrollmentResponse();

        response.setEnrollmentId(1L);
        response.setStudentId(1L);
        response.setCourseId(1L);
        response.setEnrollmentDate(
                LocalDate.of(2026, 9, 1)
        );
        response.setStatus(EnrollmentStatus.valueOf("ACTIVE"));
        response.setProgress(0);


        when(
                enrollmentService.createEnrollment(
                        any(EnrollmentRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/enrollments")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.enrollmentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.courseId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.enrollmentDate")
                                .value("2026-09-01")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("ACTIVE")
                )
                .andExpect(
                        jsonPath("$.progress")
                                .value(0)
                );
    }


    // =========================================================
    // 2. GET ALL ENROLLMENTS
    // =========================================================

    @Test
    void testGetAllEnrollments() throws Exception {

        EnrollmentResponse enrollment1 =
                new EnrollmentResponse();

        enrollment1.setEnrollmentId(1L);
        enrollment1.setStudentId(1L);
        enrollment1.setCourseId(1L);
        enrollment1.setEnrollmentDate(
                LocalDate.of(2026, 9, 1)
        );
        enrollment1.setStatus(EnrollmentStatus.valueOf("ACTIVE"));
        enrollment1.setProgress(0);


        EnrollmentResponse enrollment2 =
                new EnrollmentResponse();

        enrollment2.setEnrollmentId(2L);
        enrollment2.setStudentId(2L);
        enrollment2.setCourseId(2L);
        enrollment2.setEnrollmentDate(
                LocalDate.of(2026, 9, 2)
        );
        enrollment2.setStatus(EnrollmentStatus.valueOf("ACTIVE"));
        enrollment2.setProgress(25);


        when(
                enrollmentService.getAllEnrollments()
        ).thenReturn(
                List.of(
                        enrollment1,
                        enrollment2
                )
        );


        mockMvc.perform(
                        get("/api/enrollments")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].enrollmentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].courseId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].status")
                                .value("ACTIVE")
                )
                .andExpect(
                        jsonPath("$[1].enrollmentId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].studentId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].courseId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].progress")
                                .value(25)
                );
    }


    // =========================================================
    // 3. GET ENROLLMENT BY ID
    // =========================================================

    @Test
    void testGetEnrollmentById() throws Exception {

        EnrollmentResponse response =
                new EnrollmentResponse();

        response.setEnrollmentId(1L);
        response.setStudentId(1L);
        response.setCourseId(1L);
        response.setEnrollmentDate(
                LocalDate.of(2026, 9, 1)
        );
        response.setStatus(EnrollmentStatus.valueOf("ACTIVE"));
        response.setProgress(0);


        when(
                enrollmentService.getEnrollmentById(1L)
        ).thenReturn(response);


        mockMvc.perform(
                        get("/api/enrollments/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.enrollmentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.courseId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.enrollmentDate")
                                .value("2026-09-01")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("ACTIVE")
                )
                .andExpect(
                        jsonPath("$.progress")
                                .value(0)
                );
    }


    // =========================================================
    // 4. DELETE ENROLLMENT
    // =========================================================

    @Test
    void testDeleteEnrollment() throws Exception {

        /*
         * deleteEnrollment() returns void,
         * so we use doNothing().
         */
        doNothing()
                .when(enrollmentService)
                .deleteEnrollment(1L);


        mockMvc.perform(
                        delete("/api/enrollments/1")
                )
                .andExpect(status().isNoContent());
    }
}