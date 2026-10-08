package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.CourseRequest;
import com.spring.course.management.system.dto.CourseResponse;
import com.spring.course.management.system.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Course Management",
        description = "APIs for creating, retrieving, updating, and deleting courses"
)
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // CREATE
    @Operation(
            summary = "Create a new course",
            description = "Creates a new course in the system"
    )
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response =
                courseService.createCourse(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @Operation(
            summary = "Get all courses",
            description = "Retrieves all courses available in the system"
    )
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {

        List<CourseResponse> courses =
                courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    // GET BY ID
    @Operation(
            summary = "Get course by ID",
            description = "Retrieves a specific course using its course ID"
    )
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable Long courseId) {

        CourseResponse response =
                courseService.getCourseById(courseId);

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @Operation(
            summary = "Update a course",
            description = "Updates an existing course using its course ID"
    )
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response =
                courseService.updateCourse(courseId, request);

        return ResponseEntity.ok(response);
    }

    // DELETE
    @Operation(
            summary = "Delete a course",
            description = "Deletes an existing course using its course ID"
    )
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId) {

        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }
}