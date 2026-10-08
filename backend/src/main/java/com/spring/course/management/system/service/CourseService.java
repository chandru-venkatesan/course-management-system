package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.CourseRequest;
import com.spring.course.management.system.dto.CourseResponse;
import com.spring.course.management.system.exception.CourseNotFoundException;
import com.spring.course.management.system.exception.PlatformNotFoundException;
import com.spring.course.management.system.exception.TrainerNotFoundException;
import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.Platform;
import com.spring.course.management.system.repository.CourseRepository;
import com.spring.course.management.system.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import com.spring.course.management.system.model.Trainer;
import com.spring.course.management.system.repository.TrainerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Service
public class CourseService {

    private final TrainerRepository trainerRepository;

    private final CourseRepository courseRepository;

    private final PlatformRepository platformRepository;

    public CourseService(
            CourseRepository courseRepository,
            TrainerRepository trainerRepository,
            PlatformRepository platformRepository) {

        this.courseRepository = courseRepository;
        this.trainerRepository = trainerRepository;
        this.platformRepository = platformRepository;
    }

    // CREATE
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse createCourse(CourseRequest request) {

        Course course = new Course();

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());

        if (request.getTrainerId() != null) {

            Trainer trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() ->
                            new TrainerNotFoundException(
                                    "Trainer not found with ID: "
                                            + request.getTrainerId()
                            ));

            course.setTrainer(trainer);
        }

        if (request.getPlatformId() != null) {

            Platform platform = platformRepository
                    .findById(request.getPlatformId())
                    .orElseThrow(() ->
                            new PlatformNotFoundException(
                                    "Platform not found with ID: "
                                            + request.getPlatformId()
                            ));

            course.setPlatform(platform);
        }

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    // GET ALL
    @Cacheable("courses")
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY ID
    public CourseResponse getCourseById(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + courseId
                        )
                );

        return mapToResponse(course);
    }

    // UPDATE
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse updateCourse(
            Long courseId,
            CourseRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + courseId
                        )
                );

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());

        if (request.getTrainerId() != null) {

            Trainer trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() ->
                            new TrainerNotFoundException(
                                    "Trainer not found with ID: "
                                            + request.getTrainerId()
                            ));

            course.setTrainer(trainer);
        }

        if (request.getPlatformId() != null) {

            Platform platform = platformRepository
                    .findById(request.getPlatformId())
                    .orElseThrow(() ->
                            new PlatformNotFoundException(
                                    "Platform not found with ID: "
                                            + request.getPlatformId()
                            ));

            course.setPlatform(platform);
        }

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    // DELETE
    @CacheEvict(value = "courses", allEntries = true)
    public void deleteCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: " + courseId
                        )
                );

        courseRepository.delete(course);
    }

    // ENTITY → RESPONSE DTO
    private CourseResponse mapToResponse(Course course) {

        Long trainerId = null;
        String trainerName = null;

        if (course.getTrainer() != null) {
            trainerId = course.getTrainer().getTrainerId();
            trainerName = course.getTrainer().getTrainerName();
        }

        Long platformId = null;
        String platformName = null;

        if (course.getPlatform() != null) {
            platformId = course.getPlatform().getPlatformId();
            platformName = course.getPlatform().getPlatformName();
        }

        return new CourseResponse(
                course.getCourseId(),
                course.getCourseName(),
                course.getDescription(),
                course.getPrice(),
                course.getDuration(),
                trainerId,
                trainerName,
                platformId,
                platformName
        );
    }
}

