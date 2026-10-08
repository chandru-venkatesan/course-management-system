package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository
        extends JpaRepository<Student, Long> {
}
