package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_StudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );
}
