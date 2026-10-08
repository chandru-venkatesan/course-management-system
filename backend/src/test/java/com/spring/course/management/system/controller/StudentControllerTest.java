package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.StudentRequest;
import com.spring.course.management.system.dto.StudentResponse;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.rate.RateLimitService;
import com.spring.course.management.system.service.StudentService;

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

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * We create ObjectMapper manually.
     *
     * This avoids the previous error:
     * No qualifying bean of type 'ObjectMapper'
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock StudentService.
     *
     * The controller will use this mock instead of the real service.
     */
    @MockitoBean
    private StudentService studentService;

    /*
     * Mock JWT filter because this is a controller test.
     */
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * Mock RateLimitService because security/rate limiting
     * should not interfere with controller unit testing.
     */
    @MockitoBean
    private RateLimitService rateLimitService;


    // =========================================================
    // 1. CREATE STUDENT
    // =========================================================

    @Test
    void testCreateStudent() throws Exception {

        StudentRequest request =
                new StudentRequest();

        request.setStudentName("Arun Kumar");
        request.setEmail("arun@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDateOfBirth("2000-05-15");


        StudentResponse response =
                new StudentResponse();

        response.setStudentId(1L);
        response.setStudentName("Arun Kumar");
        response.setEmail("arun@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDateOfBirth("2000-05-15");


        /*
         * When controller calls:
         *
         * studentService.createStudent(request)
         *
         * return our mocked response.
         */
        when(studentService.createStudent(any(StudentRequest.class)))
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/students")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.studentName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arun@gmail.com")
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value("9876543210")
                )
                .andExpect(
                        jsonPath("$.dateOfBirth")
                                .value("2000-05-15")
                );
    }


    // =========================================================
    // 2. GET ALL STUDENTS
    // =========================================================

    @Test
    void testGetAllStudents() throws Exception {

        StudentResponse student1 =
                new StudentResponse();

        student1.setStudentId(1L);
        student1.setStudentName("Arun Kumar");
        student1.setEmail("arun@gmail.com");
        student1.setPhoneNumber("9876543210");
        student1.setDateOfBirth("2000-05-15");


        StudentResponse student2 =
                new StudentResponse();

        student2.setStudentId(2L);
        student2.setStudentName("Priya");
        student2.setEmail("priya@gmail.com");
        student2.setPhoneNumber("9876543211");
        student2.setDateOfBirth("2001-06-20");


        when(studentService.getAllStudents())
                .thenReturn(
                        List.of(
                                student1,
                                student2
                        )
                );


        mockMvc.perform(
                        get("/api/students")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].studentName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$[0].email")
                                .value("arun@gmail.com")
                )
                .andExpect(
                        jsonPath("$[1].studentId")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].studentName")
                                .value("Priya")
                )
                .andExpect(
                        jsonPath("$[1].email")
                                .value("priya@gmail.com")
                );
    }


    // =========================================================
    // 3. GET STUDENT BY ID
    // =========================================================

    @Test
    void testGetStudentById() throws Exception {

        StudentResponse response =
                new StudentResponse();

        response.setStudentId(1L);
        response.setStudentName("Arun Kumar");
        response.setEmail("arun@gmail.com");
        response.setPhoneNumber("9876543210");
        response.setDateOfBirth("2000-05-15");


        when(studentService.getStudentById(1L))
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/students/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.studentName")
                                .value("Arun Kumar")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arun@gmail.com")
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value("9876543210")
                )
                .andExpect(
                        jsonPath("$.dateOfBirth")
                                .value("2000-05-15")
                );
    }


    // =========================================================
    // 4. UPDATE STUDENT
    // =========================================================

    @Test
    void testUpdateStudent() throws Exception {

        StudentRequest request =
                new StudentRequest();

        request.setStudentName("Arun Kumar Updated");
        request.setEmail("arunupdated@gmail.com");
        request.setPhoneNumber("9999999999");
        request.setDateOfBirth("2000-05-15");


        StudentResponse response =
                new StudentResponse();

        response.setStudentId(1L);
        response.setStudentName("Arun Kumar Updated");
        response.setEmail("arunupdated@gmail.com");
        response.setPhoneNumber("9999999999");
        response.setDateOfBirth("2000-05-15");


        when(
                studentService.updateStudent(
                        eq(1L),
                        any(StudentRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        put("/api/students/1")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.studentId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.studentName")
                                .value("Arun Kumar Updated")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("arunupdated@gmail.com")
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value("9999999999")
                )
                .andExpect(
                        jsonPath("$.dateOfBirth")
                                .value("2000-05-15")
                );
    }


    // =========================================================
    // 5. DELETE STUDENT
    // =========================================================

    @Test
    void testDeleteStudent() throws Exception {

        /*
         * deleteStudent() returns void.
         *
         * Therefore we use doNothing().
         */
        doNothing()
                .when(studentService)
                .deleteStudent(1L);


        mockMvc.perform(
                        delete("/api/students/1")
                )
                .andExpect(status().isNoContent());
    }
}