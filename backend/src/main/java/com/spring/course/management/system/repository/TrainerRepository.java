package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
