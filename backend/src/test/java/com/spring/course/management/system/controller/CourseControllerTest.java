package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.CourseResponse;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.service.CourseService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spring.course.management.system.dto.CourseRequest;
import org.springframework.http.MediaType;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private RateLimitService rateLimitService;

    @Test
    void testGetAllCourses() throws Exception {

        CourseResponse course = new CourseResponse(
                1L,
                "Java Full Stack",
                "Java and Spring Boot",
                15000,
                "6 Months",
                null,
                null,
                null,
                null
        );

        when(courseService.getAllCourses())
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].courseName")
                        .value("Java Full Stack"))
                .andExpect(jsonPath("$[0].price")
                        .value(15000));

        verify(courseService, times(1))
                .getAllCourses();
    }

    @Test
    void testCreateCourse() throws Exception {

        CourseResponse course = new CourseResponse(
                1L,
                "Java Full Stack",
                "Java and Spring Boot",
                15000,
                "6 Months",
                null,
                null,
                null,
                null
        );

        when(courseService.createCourse(any(CourseRequest.class)))
                .thenReturn(course);

        mockMvc.perform(
                        post("/api/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "courseName": "Java Full Stack",
                                        "description": "Java and Spring Boot",
                                        "price": 15000,
                                        "duration": "6 Months"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseName")
                        .value("Java Full Stack"))
                .andExpect(jsonPath("$.price")
                        .value(15000));

        verify(courseService, times(1))
                .createCourse(any(CourseRequest.class));
    }

    @Test
    void testGetCourseById() throws Exception {

        CourseResponse course = new CourseResponse(
                1L,
                "Java Full Stack",
                "Java and Spring Boot",
                15000,
                "6 Months",
                null,
                null,
                null,
                null
        );

        when(courseService.getCourseById(1L))
                .thenReturn(course);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseName")
                        .value("Java Full Stack"))
                .andExpect(jsonPath("$.price")
                        .value(15000));

        verify(courseService, times(1))
                .getCourseById(1L);
    }

    @Test
    void testUpdateCourse() throws Exception {

        CourseResponse course = new CourseResponse(
                1L,
                "Advanced Java Full Stack",
                "Advanced Java and Spring Boot",
                18000,
                "8 Months",
                null,
                null,
                null,
                null
        );

        when(courseService.updateCourse(
                eq(1L),
                any(CourseRequest.class)
        )).thenReturn(course);

        mockMvc.perform(
                        put("/api/courses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "courseName": "Advanced Java Full Stack",
                                        "description": "Advanced Java and Spring Boot",
                                        "price": 18000,
                                        "duration": "8 Months"
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseName")
                        .value("Advanced Java Full Stack"))
                .andExpect(jsonPath("$.price")
                        .value(18000));

        verify(courseService, times(1))
                .updateCourse(
                        eq(1L),
                        any(CourseRequest.class)
                );
    }

    @Test
    void testDeleteCourse() throws Exception {

        doNothing()
                .when(courseService)
                .deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1))
                .deleteCourse(1L);
    }

    @Test
    void testCreateCourseWithInvalidData() throws Exception {

        mockMvc.perform(
                        post("/api/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "courseName": "",
                                        "description": "",
                                        "price": -100,
                                        "duration": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verify(courseService, never())
                .createCourse(any(CourseRequest.class));
    }
}