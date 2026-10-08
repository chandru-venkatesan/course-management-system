package com.spring.course.management.system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.StudentRequest;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.repository.StudentRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();


    // =========================================================
    // TEST 1 — CREATE STUDENT
    // =========================================================

    @Test
    void testCreateStudent() throws Exception {

        StudentRequest request =
                new StudentRequest();

        request.setStudentName(
                "Integration Test Student"
        );

        request.setEmail(
                "integration.student@test.com"
        );

        request.setPhoneNumber(
                "9876543210"
        );

        request.setDateOfBirth(
                "15-08-2000"
        );


        mockMvc.perform(
                        post("/api/students")
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
                        jsonPath("$.studentName")
                                .value(
                                        "Integration Test Student"
                                )
                )
                .andExpect(
                        jsonPath("$.email")
                                .value(
                                        "integration.student@test.com"
                                )
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value(
                                        "9876543210"
                                )
                )
                .andExpect(
                        jsonPath("$.dateOfBirth")
                                .value(
                                        "15-08-2000"
                                )
                );
    }


    // =========================================================
    // TEST 2 — GET ALL STUDENTS
    // =========================================================

    @Test
    void testGetAllStudents() throws Exception {

        mockMvc.perform(
                        get("/api/students")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$").isArray()
                );
    }


    // =========================================================
    // TEST 3 — GET STUDENT BY ID
    // =========================================================

    @Test
    void testGetStudentById() throws Exception {

        Student student =
                new Student();

        student.setStudentName(
                "GET Test Student"
        );

        student.setEmail(
                "get.test.student@test.com"
        );

        student.setPhoneNumber(
                "9876500001"
        );

        student.setDateOfBirth(
                "10-10-2001"
        );


        Student savedStudent =
                studentRepository.save(student);


        mockMvc.perform(
                        get(
                                "/api/students/"
                                        + savedStudent.getStudentId()
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.studentId")
                                .value(
                                        savedStudent.getStudentId()
                                )
                )
                .andExpect(
                        jsonPath("$.studentName")
                                .value(
                                        "GET Test Student"
                                )
                )
                .andExpect(
                        jsonPath("$.email")
                                .value(
                                        "get.test.student@test.com"
                                )
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value(
                                        "9876500001"
                                )
                );
    }


    // =========================================================
    // TEST 4 — UPDATE STUDENT
    // =========================================================

    @Test
    void testUpdateStudent() throws Exception {

        Student student =
                new Student();

        student.setStudentName(
                "Old Student Name"
        );

        student.setEmail(
                "old.student@test.com"
        );

        student.setPhoneNumber(
                "9000000001"
        );

        student.setDateOfBirth(
                "01-01-2000"
        );


        Student savedStudent =
                studentRepository.save(student);


        StudentRequest request =
                new StudentRequest();

        request.setStudentName(
                "Updated Student Name"
        );

        request.setEmail(
                "updated.student@test.com"
        );

        request.setPhoneNumber(
                "9000000002"
        );

        request.setDateOfBirth(
                "02-02-2000"
        );


        mockMvc.perform(
                        put(
                                "/api/students/"
                                        + savedStudent.getStudentId()
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
                        jsonPath("$.studentName")
                                .value(
                                        "Updated Student Name"
                                )
                )
                .andExpect(
                        jsonPath("$.email")
                                .value(
                                        "updated.student@test.com"
                                )
                )
                .andExpect(
                        jsonPath("$.phoneNumber")
                                .value(
                                        "9000000002"
                                )
                )
                .andExpect(
                        jsonPath("$.dateOfBirth")
                                .value(
                                        "02-02-2000"
                                )
                );
    }


    // =========================================================
    // TEST 5 — DELETE STUDENT
    // =========================================================

    @Test
    void testDeleteStudent() throws Exception {

        Student student =
                new Student();

        student.setStudentName(
                "Student To Delete"
        );

        student.setEmail(
                "delete.student@test.com"
        );

        student.setPhoneNumber(
                "9111111111"
        );

        student.setDateOfBirth(
                "03-03-2000"
        );


        Student savedStudent =
                studentRepository.save(student);


        Long studentId =
                savedStudent.getStudentId();


        mockMvc.perform(
                        delete(
                                "/api/students/"
                                        + studentId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );


        Optional<Student> deletedStudent =
                studentRepository.findById(
                        studentId
                );


        assertTrue(
                deletedStudent.isEmpty()
        );
    }
}