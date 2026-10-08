package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.SubscriptionRequest;
import com.spring.course.management.system.dto.SubscriptionResponse;
import com.spring.course.management.system.model.PaymentStatus;
import com.spring.course.management.system.model.SubscriptionStatus;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.SubscriptionService;

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

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Create ObjectMapper manually.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock SubscriptionService.
     */
    @MockitoBean
    private SubscriptionService subscriptionService;

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
    // 1. CREATE SUBSCRIPTION
    // =========================================================

    @Test
    void testCreateSubscription() throws Exception {

        SubscriptionRequest request =
                new SubscriptionRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        SubscriptionResponse response =
                new SubscriptionResponse();

        response.setSubscriptionId(1L);
        response.setStudentId(1L);
        response.setCourseId(1L);

        response.setStartDate(
                LocalDate.of(2026, 9, 1)
        );

        response.setEndDate(
                LocalDate.of(2027, 3, 1)
        );

        response.setStatus(SubscriptionStatus.valueOf("ACTIVE"));
        response.setPaymentStatus(PaymentStatus.valueOf("PAID"));
        response.setAmount(15000.0);


        when(
                subscriptionService.createSubscription(
                        any(SubscriptionRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/subscriptions")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.subscriptionId")
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
                        jsonPath("$.startDate")
                                .value("2026-09-01")
                )
                .andExpect(
                        jsonPath("$.endDate")
                                .value("2027-03-01")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("ACTIVE")
                )
                .andExpect(
                        jsonPath("$.paymentStatus")
                                .value("PAID")
                )
                .andExpect(
                        jsonPath("$.amount")
                                .value(15000.0)
                );
    }


    // =========================================================
    // 2. GET ALL SUBSCRIPTIONS
    // =========================================================

    @Test
    void testGetAllSubscriptions() throws Exception {

        SubscriptionResponse subscription1 =
                new SubscriptionResponse();

        subscription1.setSubscriptionId(1L);
        subscription1.setStudentId(1L);
        subscription1.setCourseId(1L);

        subscription1.setStartDate(
                LocalDate.of(2026, 9, 1)
        );

        subscription1.setEndDate(
                LocalDate.of(2027, 3, 1)
        );

        subscription1.setStatus(SubscriptionStatus.valueOf("ACTIVE"));
        subscription1.setPaymentStatus(PaymentStatus.valueOf("PAID"));
        subscription1.setAmount(15000.0);


        SubscriptionResponse subscription2 =
                new SubscriptionResponse();

        subscription2.setSubscriptionId(2L);
        subscription2.setStudentId(2L);
        subscription2.setCourseId(2L);

        subscription2.setStartDate(
                LocalDate.of(2026, 9, 2)
        );

        subscription2.setEndDate(
                LocalDate.of(2027, 3, 2)
        );

        subscription2.setStatus(SubscriptionStatus.valueOf("ACTIVE"));
        subscription2.setPaymentStatus(PaymentStatus.valueOf("PAID"));
        subscription2.setAmount(20000.0);


        when(
                subscriptionService.getAllSubscriptions()
        ).thenReturn(
                List.of(
                        subscription1,
                        subscription2
                )
        );


        mockMvc.perform(
                        get("/api/subscriptions")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].subscriptionId")
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
                        jsonPath("$[0].paymentStatus")
                                .value("PAID")
                )
                .andExpect(
                        jsonPath("$[1].subscriptionId")
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
                        jsonPath("$[1].amount")
                                .value(20000.0)
                );
    }


    // =========================================================
    // 3. GET SUBSCRIPTION BY ID
    // =========================================================

    @Test
    void testGetSubscriptionById() throws Exception {

        SubscriptionResponse response =
                new SubscriptionResponse();

        response.setSubscriptionId(1L);
        response.setStudentId(1L);
        response.setCourseId(1L);

        response.setStartDate(
                LocalDate.of(2026, 9, 1)
        );

        response.setEndDate(
                LocalDate.of(2027, 3, 1)
        );

        response.setStatus(SubscriptionStatus.valueOf("ACTIVE"));
        response.setPaymentStatus(PaymentStatus.valueOf("PAID"));
        response.setAmount(15000.0);


        when(
                subscriptionService.getSubscriptionById(1L)
        ).thenReturn(response);


        mockMvc.perform(
                        get("/api/subscriptions/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.subscriptionId")
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
                        jsonPath("$.startDate")
                                .value("2026-09-01")
                )
                .andExpect(
                        jsonPath("$.endDate")
                                .value("2027-03-01")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("ACTIVE")
                )
                .andExpect(
                        jsonPath("$.paymentStatus")
                                .value("PAID")
                )
                .andExpect(
                        jsonPath("$.amount")
                                .value(15000.0)
                );
    }


    // =========================================================
    // 4. DELETE SUBSCRIPTION
    // =========================================================

    @Test
    void testDeleteSubscription() throws Exception {

        /*
         * deleteSubscription() returns void,
         * so we use doNothing().
         */
        doNothing()
                .when(subscriptionService)
                .deleteSubscription(1L);


        mockMvc.perform(
                        delete("/api/subscriptions/1")
                )
                .andExpect(status().isNoContent());
    }
}