package com.spring.course.management.system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.CourseRequest;
import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.repository.CourseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();


    // =========================================================
    // TEST 1 — CREATE COURSE
    // =========================================================

    @Test
    void testCreateCourse() throws Exception {

        CourseRequest request =
                new CourseRequest();

        request.setCourseName(
                "Integration Testing with Spring Boot"
        );

        request.setDescription(
                "Course created during integration testing"
        );

        request.setPrice(4999.0);

        request.setDuration(
                "8 Weeks"
        );


        mockMvc.perform(
                        post("/api/courses")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.courseName")
                                .value(
                                        "Integration Testing with Spring Boot"
                                )
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Course created during integration testing"
                                )
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(4999.0)
                )
                .andExpect(
                        jsonPath("$.duration")
                                .value("8 Weeks")
                );
    }


    // =========================================================
    // TEST 2 — GET ALL COURSES
    // =========================================================

    @Test
    void testGetAllCourses() throws Exception {

        mockMvc.perform(
                        get("/api/courses")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$").isArray()
                );
    }


    // =========================================================
    // TEST 3 — GET COURSE BY ID
    // =========================================================

    @Test
    void testGetCourseById() throws Exception {

        Course course =
                new Course();

        course.setCourseName(
                "Integration Test Course"
        );

        course.setDescription(
                "Course used for GET by ID integration test"
        );

        course.setPrice(2999.0);

        course.setDuration(
                "6 Weeks"
        );

        Course savedCourse =
                courseRepository.save(course);


        mockMvc.perform(
                        get(
                                "/api/courses/"
                                        + savedCourse.getCourseId()
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.courseId")
                                .value(
                                        savedCourse.getCourseId()
                                )
                )
                .andExpect(
                        jsonPath("$.courseName")
                                .value(
                                        "Integration Test Course"
                                )
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(2999.0)
                );
    }


    // =========================================================
    // TEST 4 — UPDATE COURSE
    // =========================================================

    @Test
    void testUpdateCourse() throws Exception {

        Course course =
                new Course();

        course.setCourseName(
                "Old Course Name"
        );

        course.setDescription(
                "Old course description"
        );

        course.setPrice(1999.0);

        course.setDuration(
                "4 Weeks"
        );

        Course savedCourse =
                courseRepository.save(course);


        CourseRequest request =
                new CourseRequest();

        request.setCourseName(
                "Updated Course Name"
        );

        request.setDescription(
                "Updated course description"
        );

        request.setPrice(3999.0);

        request.setDuration(
                "8 Weeks"
        );


        mockMvc.perform(
                        put(
                                "/api/courses/"
                                        + savedCourse.getCourseId()
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.courseName")
                                .value(
                                        "Updated Course Name"
                                )
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Updated course description"
                                )
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(3999.0)
                )
                .andExpect(
                        jsonPath("$.duration")
                                .value("8 Weeks")
                );
    }


    // =========================================================
    // TEST 5 — DELETE COURSE
    // =========================================================

    @Test
    void testDeleteCourse() throws Exception {

        Course course =
                new Course();

        course.setCourseName(
                "Course To Delete"
        );

        course.setDescription(
                "Temporary course for delete test"
        );

        course.setPrice(999.0);

        course.setDuration(
                "2 Weeks"
        );

        Course savedCourse =
                courseRepository.save(course);


        Long courseId =
                savedCourse.getCourseId();


        mockMvc.perform(
                        delete(
                                "/api/courses/"
                                        + courseId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );


        Optional<Course> deletedCourse =
                courseRepository.findById(
                        courseId
                );


        org.junit.jupiter.api.Assertions
                .assertTrue(
                        deletedCourse.isEmpty()
                );
    }
}