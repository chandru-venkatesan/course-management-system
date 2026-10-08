package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.StudentRequest;
import com.spring.course.management.system.dto.StudentResponse;
import com.spring.course.management.system.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.createStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> students =
                studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    // GET BY ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long studentId) {

        StudentResponse response =
                studentService.getStudentById(studentId);

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.updateStudent(
                        studentId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long studentId) {

        studentService.deleteStudent(studentId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
