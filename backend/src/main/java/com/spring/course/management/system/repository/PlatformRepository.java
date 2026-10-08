package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepository
        extends JpaRepository<Platform, Long> {
}
