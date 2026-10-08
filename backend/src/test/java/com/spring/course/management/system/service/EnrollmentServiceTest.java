package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.EnrollmentRequest;
import com.spring.course.management.system.dto.EnrollmentResponse;
import com.spring.course.management.system.exception.CourseNotFoundException;
import com.spring.course.management.system.exception.EnrollmentNotFoundException;
import com.spring.course.management.system.exception.StudentNotFoundException;
import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.Enrollment;
import com.spring.course.management.system.model.EnrollmentStatus;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.repository.CourseRepository;
import com.spring.course.management.system.repository.EnrollmentRepository;
import com.spring.course.management.system.repository.StudentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;


    // ---------------------------------------------------------
    // 1. Test Get All Enrollments
    // ---------------------------------------------------------

    @Test
    void testGetAllEnrollments() {

        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");


        Enrollment enrollment1 = new Enrollment();

        enrollment1.setEnrollmentId(1L);
        enrollment1.setStudent(student);
        enrollment1.setCourse(course);
        enrollment1.setEnrollmentDate(
                java.time.LocalDate.of(2026, 9, 1)
        );
        enrollment1.setStatus(EnrollmentStatus.ACTIVE);
        enrollment1.setProgress(0);


        Enrollment enrollment2 = new Enrollment();

        enrollment2.setEnrollmentId(2L);
        enrollment2.setStudent(student);
        enrollment2.setCourse(course);
        enrollment2.setEnrollmentDate(
                java.time.LocalDate.of(2026, 9, 2)
        );
        enrollment2.setStatus(EnrollmentStatus.COMPLETED);
        enrollment2.setProgress(100);


        when(enrollmentRepository.findAll())
                .thenReturn(List.of(enrollment1, enrollment2));


        List<EnrollmentResponse> result =
                enrollmentService.getAllEnrollments();


        assertEquals(2, result.size());

        assertEquals(
                1L,
                result.get(0).getEnrollmentId()
        );

        assertEquals(
                "Chandru",
                result.get(0).getStudentName()
        );

        assertEquals(
                "Java Full Stack",
                result.get(0).getCourseName()
        );

        assertEquals(
                EnrollmentStatus.ACTIVE,
                result.get(0).getStatus()
        );

        assertEquals(
                EnrollmentStatus.COMPLETED,
                result.get(1).getStatus()
        );


        verify(enrollmentRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 2. Test Get Enrollment By ID
    // ---------------------------------------------------------

    @Test
    void testGetEnrollmentById() {

        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");


        Enrollment enrollment = new Enrollment();

        enrollment.setEnrollmentId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(
                java.time.LocalDate.of(2026, 9, 1)
        );
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);


        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));


        EnrollmentResponse result =
                enrollmentService.getEnrollmentById(1L);


        assertEquals(
                1L,
                result.getEnrollmentId()
        );

        assertEquals(
                1L,
                result.getStudentId()
        );

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                1L,
                result.getCourseId()
        );

        assertEquals(
                "Java Full Stack",
                result.getCourseName()
        );

        assertEquals(
                EnrollmentStatus.ACTIVE,
                result.getStatus()
        );

        assertEquals(
                0,
                result.getProgress()
        );


        verify(enrollmentRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 3. Test Get Enrollment By ID - Not Found
    // ---------------------------------------------------------

    @Test
    void testGetEnrollmentByIdEnrollmentNotFound() {

        when(enrollmentRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                EnrollmentNotFoundException.class,
                () -> enrollmentService.getEnrollmentById(999L)
        );


        verify(enrollmentRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 4. Test Create Enrollment
    // ---------------------------------------------------------

    @Test
    void testCreateEnrollment() {

        EnrollmentRequest request = new EnrollmentRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");


        Enrollment savedEnrollment = new Enrollment();

        savedEnrollment.setEnrollmentId(1L);
        savedEnrollment.setStudent(student);
        savedEnrollment.setCourse(course);
        savedEnrollment.setEnrollmentDate(
                java.time.LocalDate.now()
        );
        savedEnrollment.setStatus(EnrollmentStatus.ACTIVE);
        savedEnrollment.setProgress(0);


        when(
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                1L,
                                1L
                        )
        ).thenReturn(false);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));


        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenReturn(savedEnrollment);


        EnrollmentResponse result =
                enrollmentService.createEnrollment(request);


        assertEquals(
                1L,
                result.getEnrollmentId()
        );

        assertEquals(
                1L,
                result.getStudentId()
        );

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                1L,
                result.getCourseId()
        );

        assertEquals(
                "Java Full Stack",
                result.getCourseName()
        );

        assertEquals(
                EnrollmentStatus.ACTIVE,
                result.getStatus()
        );

        assertEquals(
                0,
                result.getProgress()
        );


        verify(
                enrollmentRepository,
                times(1)
        ).existsByStudent_StudentIdAndCourse_CourseId(
                1L,
                1L
        );


        verify(studentRepository, times(1))
                .findById(1L);


        verify(courseRepository, times(1))
                .findById(1L);


        verify(enrollmentRepository, times(1))
                .save(any(Enrollment.class));
    }


    // ---------------------------------------------------------
    // 5. Test Create Enrollment - Student Not Found
    // ---------------------------------------------------------

    @Test
    void testCreateEnrollmentStudentNotFound() {

        EnrollmentRequest request = new EnrollmentRequest();

        request.setStudentId(999L);
        request.setCourseId(1L);


        when(
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                999L,
                                1L
                        )
        ).thenReturn(false);


        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                StudentNotFoundException.class,
                () -> enrollmentService.createEnrollment(request)
        );


        verify(studentRepository, times(1))
                .findById(999L);


        verify(courseRepository, never())
                .findById(anyLong());


        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }


    // ---------------------------------------------------------
    // 6. Test Create Enrollment - Course Not Found
    // ---------------------------------------------------------

    @Test
    void testCreateEnrollmentCourseNotFound() {

        EnrollmentRequest request = new EnrollmentRequest();

        request.setStudentId(1L);
        request.setCourseId(999L);


        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        when(
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                1L,
                                999L
                        )
        ).thenReturn(false);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        when(courseRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                CourseNotFoundException.class,
                () -> enrollmentService.createEnrollment(request)
        );


        verify(studentRepository, times(1))
                .findById(1L);


        verify(courseRepository, times(1))
                .findById(999L);


        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }


    // ---------------------------------------------------------
    // 7. Test Create Enrollment - Duplicate
    // ---------------------------------------------------------

    @Test
    void testCreateEnrollmentAlreadyExists() {

        EnrollmentRequest request = new EnrollmentRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        when(
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                1L,
                                1L
                        )
        ).thenReturn(true);


        assertThrows(
                IllegalStateException.class,
                () -> enrollmentService.createEnrollment(request)
        );


        verify(
                enrollmentRepository,
                times(1)
        ).existsByStudent_StudentIdAndCourse_CourseId(
                1L,
                1L
        );


        verify(studentRepository, never())
                .findById(anyLong());


        verify(courseRepository, never())
                .findById(anyLong());


        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }


    // ---------------------------------------------------------
    // 8. Test Delete Enrollment
    // ---------------------------------------------------------

    @Test
    void testDeleteEnrollment() {

        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");


        Enrollment enrollment = new Enrollment();

        enrollment.setEnrollmentId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(
                java.time.LocalDate.of(2026, 9, 1)
        );
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);


        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));


        doNothing()
                .when(enrollmentRepository)
                .delete(enrollment);


        enrollmentService.deleteEnrollment(1L);


        verify(enrollmentRepository, times(1))
                .findById(1L);


        verify(enrollmentRepository, times(1))
                .delete(enrollment);
    }


    // ---------------------------------------------------------
    // 9. Test Delete Enrollment - Not Found
    // ---------------------------------------------------------

    @Test
    void testDeleteEnrollmentEnrollmentNotFound() {

        when(enrollmentRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                EnrollmentNotFoundException.class,
                () -> enrollmentService.deleteEnrollment(999L)
        );


        verify(enrollmentRepository, times(1))
                .findById(999L);


        verify(enrollmentRepository, never())
                .delete(any(Enrollment.class));
    }
}