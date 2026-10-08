package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.Enrollment;
import com.spring.course.management.system.model.EnrollmentStatus;
import com.spring.course.management.system.model.Student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Test
    void testSaveEnrollment() {

        Student student = createStudent("save@test.com");
        Course course = createCourse("Java Full Stack");

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        assertNotNull(savedEnrollment.getEnrollmentId());
        assertEquals(student.getStudentId(),
                savedEnrollment.getStudent().getStudentId());
        assertEquals(course.getCourseId(),
                savedEnrollment.getCourse().getCourseId());
        assertEquals(EnrollmentStatus.ACTIVE,
                savedEnrollment.getStatus());
        assertEquals(0,
                savedEnrollment.getProgress());
    }


    @Test
    void testFindAllEnrollments() {

        Student student1 = createStudent("student1@test.com");
        Course course1 = createCourse("Java");

        Student student2 = createStudent("student2@test.com");
        Course course2 = createCourse("Spring Boot");

        Enrollment enrollment1 = createEnrollment(
                student1,
                course1
        );

        Enrollment enrollment2 = createEnrollment(
                student2,
                course2
        );

        Enrollment savedEnrollment1 =
                enrollmentRepository.save(enrollment1);

        Enrollment savedEnrollment2 =
                enrollmentRepository.save(enrollment2);

        List<Enrollment> enrollments =
                enrollmentRepository.findAll();

        assertTrue(enrollments.size() >= 2);

        assertTrue(
                enrollments.stream()
                        .anyMatch(enrollment ->
                                enrollment.getEnrollmentId()
                                        .equals(savedEnrollment1.getEnrollmentId()))
        );

        assertTrue(
                enrollments.stream()
                        .anyMatch(enrollment ->
                                enrollment.getEnrollmentId()
                                        .equals(savedEnrollment2.getEnrollmentId()))
        );
    }


    @Test
    void testFindEnrollmentById() {

        Student student = createStudent("find@test.com");
        Course course = createCourse("Spring Boot");

        Enrollment enrollment =
                createEnrollment(student, course);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        Optional<Enrollment> result =
                enrollmentRepository.findById(
                        savedEnrollment.getEnrollmentId()
                );

        assertTrue(result.isPresent());

        assertEquals(
                savedEnrollment.getEnrollmentId(),
                result.get().getEnrollmentId()
        );

        assertEquals(
                student.getStudentId(),
                result.get().getStudent().getStudentId()
        );

        assertEquals(
                course.getCourseId(),
                result.get().getCourse().getCourseId()
        );
    }


    @Test
    void testFindEnrollmentByIdNotFound() {

        Optional<Enrollment> result =
                enrollmentRepository.findById(99999L);

        assertFalse(result.isPresent());
    }


    @Test
    void testUpdateEnrollment() {

        Student student = createStudent("update@test.com");
        Course course = createCourse("Java");

        Enrollment enrollment =
                createEnrollment(student, course);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        savedEnrollment.setStatus(
                EnrollmentStatus.COMPLETED
        );

        savedEnrollment.setProgress(100);

        Enrollment updatedEnrollment =
                enrollmentRepository.save(savedEnrollment);

        assertEquals(
                EnrollmentStatus.COMPLETED,
                updatedEnrollment.getStatus()
        );

        assertEquals(
                100,
                updatedEnrollment.getProgress()
        );
    }


    @Test
    void testDeleteEnrollment() {

        Student student = createStudent("delete@test.com");
        Course course = createCourse("Java");

        Enrollment enrollment =
                createEnrollment(student, course);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        Long enrollmentId =
                savedEnrollment.getEnrollmentId();

        enrollmentRepository.delete(savedEnrollment);

        Optional<Enrollment> result =
                enrollmentRepository.findById(enrollmentId);

        assertFalse(result.isPresent());
    }


    @Test
    void testExistsByStudentAndCourse() {

        Student student = createStudent("duplicate@test.com");
        Course course = createCourse("Java Full Stack");

        Enrollment enrollment =
                createEnrollment(student, course);

        enrollmentRepository.save(enrollment);

        boolean exists =
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                student.getStudentId(),
                                course.getCourseId()
                        );

        assertTrue(exists);
    }


    private Student createStudent(String email) {

        Student student = new Student();

        student.setStudentName("Test Student");
        student.setEmail(email);
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        return studentRepository.save(student);
    }


    private Course createCourse(String courseName) {

        Course course = new Course();

        course.setCourseName(courseName);
        course.setDescription("Test Course");
        course.setPrice(15000.0);
        course.setDuration("6 Months");

        return courseRepository.save(course);
    }


    private Enrollment createEnrollment(
            Student student,
            Course course) {

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);

        return enrollment;
    }
}