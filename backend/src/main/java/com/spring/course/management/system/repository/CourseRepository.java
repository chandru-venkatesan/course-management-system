package com.spring.course.management.system.repository;



import com.spring.course.management.system.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}

