package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.UserRequest;
import com.spring.course.management.system.dto.UserResponse;
import com.spring.course.management.system.model.Role;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.UserService;

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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Create ObjectMapper manually.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock UserService.
     */
    @MockitoBean
    private UserService userService;

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
    // 1. CREATE USER
    // =========================================================

    @Test
    void testCreateUser() throws Exception {

        UserRequest request =
                new UserRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("password123");
        request.setRole(Role.STUDENT);


        UserResponse response =
                new UserResponse();

        response.setUserId(1L);
        response.setEmail("student@gmail.com");
        response.setRole(Role.STUDENT);
        response.setActive(true);


        when(
                userService.createUser(
                        any(UserRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/users")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.userId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("student@gmail.com")
                )
                .andExpect(
                        jsonPath("$.role")
                                .value("STUDENT")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );
    }


    // =========================================================
    // 2. GET ALL USERS
    // =========================================================

    @Test
    void testGetAllUsers() throws Exception {

        UserResponse user1 =
                new UserResponse();

        user1.setUserId(1L);
        user1.setEmail("student@gmail.com");
        user1.setRole(Role.STUDENT);
        user1.setActive(true);


        UserResponse user2 =
                new UserResponse();

        user2.setUserId(2L);
        user2.setEmail("trainer@gmail.com");
        user2.setRole(Role.TRAINER);
        user2.setActive(true);


        when(
                userService.getAllUsers()
        ).thenReturn(
                List.of(
                        user1,
                        user2
                )
        );


        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].userId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].email")
                                .value("student@gmail.com")
                )
                .andExpect(
                        jsonPath("$[0].role")
                                .value("STUDENT")
                )
                .andExpect(
                        jsonPath("$[1].userId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].email")
                                .value("trainer@gmail.com")
                )
                .andExpect(
                        jsonPath("$[1].role")
                                .value("TRAINER")
                );
    }


    // =========================================================
    // 3. GET USER BY ID
    // =========================================================

    @Test
    void testGetUserById() throws Exception {

        UserResponse response =
                new UserResponse();

        response.setUserId(1L);
        response.setEmail("student@gmail.com");
        response.setRole(Role.STUDENT);
        response.setActive(true);


        when(
                userService.getUserById(1L)
        ).thenReturn(response);


        mockMvc.perform(
                        get("/api/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.userId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("student@gmail.com")
                )
                .andExpect(
                        jsonPath("$.role")
                                .value("STUDENT")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );
    }


    // =========================================================
    // 4. UPDATE USER
    // =========================================================

    @Test
    void testUpdateUser() throws Exception {

        UserRequest request =
                new UserRequest();

        request.setEmail("student.updated@gmail.com");
        request.setPassword("newpassword123");
        request.setRole(Role.STUDENT);


        UserResponse response =
                new UserResponse();

        response.setUserId(1L);
        response.setEmail("student.updated@gmail.com");
        response.setRole(Role.STUDENT);
        response.setActive(true);


        when(
                userService.updateUser(
                        eq(1L),
                        any(UserRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        put("/api/users/1")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.userId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("student.updated@gmail.com")
                )
                .andExpect(
                        jsonPath("$.role")
                                .value("STUDENT")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );
    }


    // =========================================================
    // 5. DELETE USER
    // =========================================================

    @Test
    void testDeleteUser() throws Exception {

        /*
         * deleteUser() returns void,
         * so we use doNothing().
         */
        doNothing()
                .when(userService)
                .deleteUser(1L);


        mockMvc.perform(
                        delete("/api/users/1")
                )
                .andExpect(status().isNoContent());
    }
}