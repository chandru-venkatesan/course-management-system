package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;


    // ---------------------------------------------------------
    // 1. SAVE COURSE
    // ---------------------------------------------------------

    @Test
    void testSaveCourse() {

        Course course = new Course();

        course.setCourseName("Java Full Stack");
        course.setDescription("Complete Java Full Stack Course");
        course.setPrice(15000.0);
        course.setDuration("6 Months");

        Course savedCourse =
                courseRepository.save(course);

        assertNotNull(savedCourse.getCourseId());

        assertEquals(
                "Java Full Stack",
                savedCourse.getCourseName()
        );

        assertEquals(
                15000.0,
                savedCourse.getPrice()
        );
    }


    // ---------------------------------------------------------
    // 2. FIND ALL COURSES
    // ---------------------------------------------------------

    @Test
    void testFindAllCourses() {
        Course course1 = new Course();
        course1.setCourseName("Java Full Stack");
        course1.setDescription("Java course");
        course1.setPrice(15000.0);
        course1.setDuration("6 Months");

        Course course2 = new Course();
        course2.setCourseName("Spring Boot");
        course2.setDescription("Spring Boot course");
        course2.setPrice(10000.0);
        course2.setDuration("4 Months");

        Course savedCourse1 = courseRepository.save(course1);
        Course savedCourse2 = courseRepository.save(course2);

        List<Course> courses = courseRepository.findAll();

        assertTrue(courses.size() >= 2);

        assertTrue(
                courses.stream()
                        .anyMatch(course ->
                                course.getCourseId().equals(savedCourse1.getCourseId()))
        );

        assertTrue(
                courses.stream()
                        .anyMatch(course ->
                                course.getCourseId().equals(savedCourse2.getCourseId()))
        );
    }

    // ---------------------------------------------------------
    // 3. FIND COURSE BY ID
    // ---------------------------------------------------------

    @Test
    void testFindCourseById() {

        Course course = new Course();

        course.setCourseName("Java Full Stack");
        course.setDescription("Complete Java course");
        course.setPrice(15000.0);
        course.setDuration("6 Months");


        Course savedCourse =
                courseRepository.save(course);


        Optional<Course> result =
                courseRepository.findById(
                        savedCourse.getCourseId()
                );


        assertTrue(result.isPresent());

        assertEquals(
                "Java Full Stack",
                result.get().getCourseName()
        );

        assertEquals(
                15000.0,
                result.get().getPrice()
        );
    }


    // ---------------------------------------------------------
    // 4. FIND COURSE BY INVALID ID
    // ---------------------------------------------------------

    @Test
    void testFindCourseByIdNotFound() {

        Optional<Course> result =
                courseRepository.findById(99999L);

        assertFalse(result.isPresent());
    }


    // ---------------------------------------------------------
    // 5. UPDATE COURSE
    // ---------------------------------------------------------

    @Test
    void testUpdateCourse() {

        Course course = new Course();

        course.setCourseName("Java Full Stack");
        course.setDescription("Original description");
        course.setPrice(15000.0);
        course.setDuration("6 Months");


        Course savedCourse =
                courseRepository.save(course);


        savedCourse.setCourseName(
                "Advanced Java Full Stack"
        );

        savedCourse.setPrice(18000.0);


        Course updatedCourse =
                courseRepository.save(savedCourse);


        assertEquals(
                "Advanced Java Full Stack",
                updatedCourse.getCourseName()
        );

        assertEquals(
                18000.0,
                updatedCourse.getPrice()
        );
    }


    // ---------------------------------------------------------
    // 6. DELETE COURSE
    // ---------------------------------------------------------

    @Test
    void testDeleteCourse() {

        Course course = new Course();

        course.setCourseName("Java Full Stack");
        course.setDescription("Java course");
        course.setPrice(15000.0);
        course.setDuration("6 Months");


        Course savedCourse =
                courseRepository.save(course);


        Long courseId =
                savedCourse.getCourseId();


        courseRepository.delete(savedCourse);


        Optional<Course> result =
                courseRepository.findById(courseId);


        assertFalse(result.isPresent());
    }
}