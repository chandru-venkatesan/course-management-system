package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Subscription;
import com.spring.course.management.system.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {

    boolean existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
            Long studentId,
            Long courseId,
            SubscriptionStatus status
    );
}
