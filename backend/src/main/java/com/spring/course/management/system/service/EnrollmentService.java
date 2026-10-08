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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }



    public EnrollmentResponse createEnrollment(
            EnrollmentRequest request) {

        boolean alreadyEnrolled =
                enrollmentRepository
                        .existsByStudent_StudentIdAndCourse_CourseId(
                                request.getStudentId(),
                                request.getCourseId()
                        );

        if (alreadyEnrolled) {
            throw new IllegalStateException(
                    "Student is already enrolled in this course"
            );
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: "
                                        + request.getStudentId()
                        ));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: "
                                        + request.getCourseId()
                        ));

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }

    public List<EnrollmentResponse> getAllEnrollments() {

        return enrollmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EnrollmentResponse getEnrollmentById(
            Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Enrollment not found with ID: "
                                        + enrollmentId
                        ));

        return mapToResponse(enrollment);
    }

    public void deleteEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Enrollment not found with ID: "
                                        + enrollmentId
                        ));

        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentResponse mapToResponse(
            Enrollment enrollment) {

        return new EnrollmentResponse(
                enrollment.getEnrollmentId(),

                enrollment.getStudent().getStudentId(),
                enrollment.getStudent().getStudentName(),

                enrollment.getCourse().getCourseId(),
                enrollment.getCourse().getCourseName(),

                enrollment.getEnrollmentDate(),
                enrollment.getStatus(),
                enrollment.getProgress()
        );
    }
}
