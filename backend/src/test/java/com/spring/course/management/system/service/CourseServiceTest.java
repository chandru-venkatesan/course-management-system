package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.CourseRequest;
import com.spring.course.management.system.dto.CourseResponse;
import com.spring.course.management.system.exception.CourseNotFoundException;
import com.spring.course.management.system.exception.PlatformNotFoundException;
import com.spring.course.management.system.exception.TrainerNotFoundException;
import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.Platform;
import com.spring.course.management.system.model.Trainer;
import com.spring.course.management.system.repository.CourseRepository;
import com.spring.course.management.system.repository.PlatformRepository;
import com.spring.course.management.system.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void testGetAllCourses() {

        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");
        course.setDescription("Java and Spring Boot");
        course.setPrice(15000);
        course.setDuration("6 Months");

        when(courseRepository.findAll())
                .thenReturn(List.of(course));

        List<CourseResponse> result =
                courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Full Stack",
                result.get(0).getCourseName());

        verify(courseRepository, times(1))
                .findAll();
    }

    @Test
    void testGetCourseById() {

        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");
        course.setDescription("Java and Spring Boot");
        course.setPrice(15000);
        course.setDuration("6 Months");

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        CourseResponse result =
                courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCourseId());
        assertEquals("Java Full Stack",
                result.getCourseName());
        assertEquals(15000,
                result.getPrice());
        assertEquals("6 Months",
                result.getDuration());

        verify(courseRepository, times(1))
                .findById(1L);
    }

    @Test
    void testGetCourseByIdCourseNotFound() {

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception =
                assertThrows(
                        CourseNotFoundException.class,
                        () -> courseService.getCourseById(99L)
                );

        assertEquals(
                "Course not found with ID: 99",
                exception.getMessage()
        );

        verify(courseRepository, times(1))
                .findById(99L);
    }

    @Test
    void testCreateCourse() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Spring Boot");
        request.setDescription("Spring Boot REST API");
        request.setPrice(12000.0);
        request.setDuration("4 Months");

        Course savedCourse = new Course();

        savedCourse.setCourseId(10L);
        savedCourse.setCourseName("Spring Boot");
        savedCourse.setDescription("Spring Boot REST API");
        savedCourse.setPrice(12000);
        savedCourse.setDuration("4 Months");

        when(courseRepository.save(any(Course.class)))
                .thenReturn(savedCourse);

        CourseResponse result =
                courseService.createCourse(request);

        assertNotNull(result);
        assertEquals(10L, result.getCourseId());
        assertEquals("Spring Boot", result.getCourseName());
        assertEquals("Spring Boot REST API",
                result.getDescription());
        assertEquals(12000, result.getPrice());
        assertEquals("4 Months", result.getDuration());

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }

    @Test
    void testCreateCourseWithTrainer() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Java Advanced");
        request.setDescription("Advanced Java Programming");
        request.setPrice(18000.0);
        request.setDuration("5 Months");
        request.setTrainerId(1L);

        Trainer trainer = new Trainer();

        trainer.setTrainerId(1L);
        trainer.setTrainerName("John Trainer");

        Course savedCourse = new Course();

        savedCourse.setCourseId(11L);
        savedCourse.setCourseName("Java Advanced");
        savedCourse.setDescription("Advanced Java Programming");
        savedCourse.setPrice(18000);
        savedCourse.setDuration("5 Months");
        savedCourse.setTrainer(trainer);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(savedCourse);

        CourseResponse result =
                courseService.createCourse(request);

        assertNotNull(result);
        assertEquals(11L, result.getCourseId());
        assertEquals("Java Advanced",
                result.getCourseName());

        assertEquals(1L, result.getTrainerId());
        assertEquals("John Trainer",
                result.getTrainerName());

        verify(trainerRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }

    @Test
    void testCreateCourseTrainerNotFound() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Java Advanced");
        request.setDescription("Advanced Java Programming");
        request.setPrice(18000.0);
        request.setDuration("5 Months");
        request.setTrainerId(99L);

        when(trainerRepository.findById(99L))
                .thenReturn(Optional.empty());

        TrainerNotFoundException exception =
                assertThrows(
                        TrainerNotFoundException.class,
                        () -> courseService.createCourse(request)
                );

        assertEquals(
                "Trainer not found with ID: 99",
                exception.getMessage()
        );

        verify(trainerRepository, times(1))
                .findById(99L);

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    @Test
    void testCreateCourseWithPlatform() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Spring Boot");
        request.setDescription("Spring Boot REST API");
        request.setPrice(15000.0);
        request.setDuration("4 Months");
        request.setPlatformId(1L);

        Platform platform = new Platform();

        platform.setPlatformId(1L);
        platform.setPlatformName("Ednue Technologies");

        Course savedCourse = new Course();

        savedCourse.setCourseId(12L);
        savedCourse.setCourseName("Spring Boot");
        savedCourse.setDescription("Spring Boot REST API");
        savedCourse.setPrice(15000);
        savedCourse.setDuration("4 Months");
        savedCourse.setPlatform(platform);

        when(platformRepository.findById(1L))
                .thenReturn(Optional.of(platform));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(savedCourse);

        CourseResponse result =
                courseService.createCourse(request);

        assertNotNull(result);
        assertEquals(12L, result.getCourseId());
        assertEquals("Spring Boot", result.getCourseName());

        assertEquals(1L, result.getPlatformId());
        assertEquals("Ednue Technologies",
                result.getPlatformName());

        verify(platformRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }

    @Test
    void testCreateCoursePlatformNotFound() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Spring Boot");
        request.setDescription("Spring Boot REST API");
        request.setPrice(15000.0);
        request.setDuration("4 Months");
        request.setPlatformId(99L);

        when(platformRepository.findById(99L))
                .thenReturn(Optional.empty());

        PlatformNotFoundException exception =
                assertThrows(
                        PlatformNotFoundException.class,
                        () -> courseService.createCourse(request)
                );

        assertEquals(
                "Platform not found with ID: 99",
                exception.getMessage()
        );

        verify(platformRepository, times(1))
                .findById(99L);

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    @Test
    void testUpdateCourse() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Updated Java Full Stack");
        request.setDescription("Updated Java and Spring Boot");
        request.setPrice(20000.0);
        request.setDuration("8 Months");

        Course existingCourse = new Course();

        existingCourse.setCourseId(1L);
        existingCourse.setCourseName("Java Full Stack");
        existingCourse.setDescription("Java");
        existingCourse.setPrice(15000);
        existingCourse.setDuration("6 Months");

        Course updatedCourse = new Course();

        updatedCourse.setCourseId(1L);
        updatedCourse.setCourseName("Updated Java Full Stack");
        updatedCourse.setDescription("Updated Java and Spring Boot");
        updatedCourse.setPrice(20000);
        updatedCourse.setDuration("8 Months");

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(existingCourse));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(updatedCourse);

        CourseResponse result =
                courseService.updateCourse(1L, request);

        assertNotNull(result);

        assertEquals(1L, result.getCourseId());

        assertEquals(
                "Updated Java Full Stack",
                result.getCourseName()
        );

        assertEquals(
                "Updated Java and Spring Boot",
                result.getDescription()
        );

        assertEquals(
                20000,
                result.getPrice()
        );

        assertEquals(
                "8 Months",
                result.getDuration()
        );

        verify(courseRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }

    @Test
    void testUpdateCourseCourseNotFound() {

        CourseRequest request = new CourseRequest();

        request.setCourseName("Updated Course");
        request.setDescription("Updated Description");
        request.setPrice(20000.0);
        request.setDuration("6 Months");

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception =
                assertThrows(
                        CourseNotFoundException.class,
                        () -> courseService.updateCourse(99L, request)
                );

        assertEquals(
                "Course not found with ID: 99",
                exception.getMessage()
        );

        verify(courseRepository, times(1))
                .findById(99L);

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    @Test
    void testDeleteCourse() {

        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .delete(course);
    }

    @Test
    void testDeleteCourseCourseNotFound() {

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        CourseNotFoundException exception =
                assertThrows(
                        CourseNotFoundException.class,
                        () -> courseService.deleteCourse(99L)
                );

        assertEquals(
                "Course not found with ID: 99",
                exception.getMessage()
        );

        verify(courseRepository, times(1))
                .findById(99L);

        verify(courseRepository, never())
                .delete(any(Course.class));
    }
}