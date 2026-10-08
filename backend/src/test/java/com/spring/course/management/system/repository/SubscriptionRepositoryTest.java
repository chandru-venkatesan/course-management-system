package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.PaymentStatus;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.model.Subscription;
import com.spring.course.management.system.model.SubscriptionStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Test
    void testSaveSubscription() {

        Student student = createStudent("save-sub@test.com");
        Course course = createCourse("Java Full Stack");

        Subscription subscription =
                createSubscription(student, course);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        assertNotNull(savedSubscription.getSubscriptionId());

        assertEquals(
                student.getStudentId(),
                savedSubscription.getStudent().getStudentId()
        );

        assertEquals(
                course.getCourseId(),
                savedSubscription.getCourse().getCourseId()
        );

        assertEquals(
                SubscriptionStatus.ACTIVE,
                savedSubscription.getStatus()
        );

        assertEquals(
                PaymentStatus.PAID,
                savedSubscription.getPaymentStatus()
        );

        assertEquals(
                15000.0,
                savedSubscription.getAmount()
        );
    }


    @Test
    void testFindAllSubscriptions() {

        Student student1 =
                createStudent("sub1@test.com");

        Course course1 =
                createCourse("Java");

        Student student2 =
                createStudent("sub2@test.com");

        Course course2 =
                createCourse("Spring Boot");

        Subscription subscription1 =
                createSubscription(student1, course1);

        Subscription subscription2 =
                createSubscription(student2, course2);

        Subscription savedSubscription1 =
                subscriptionRepository.save(subscription1);

        Subscription savedSubscription2 =
                subscriptionRepository.save(subscription2);

        List<Subscription> subscriptions =
                subscriptionRepository.findAll();

        assertTrue(subscriptions.size() >= 2);

        assertTrue(
                subscriptions.stream()
                        .anyMatch(subscription ->
                                subscription.getSubscriptionId()
                                        .equals(savedSubscription1.getSubscriptionId()))
        );

        assertTrue(
                subscriptions.stream()
                        .anyMatch(subscription ->
                                subscription.getSubscriptionId()
                                        .equals(savedSubscription2.getSubscriptionId()))
        );
    }


    @Test
    void testFindSubscriptionById() {

        Student student =
                createStudent("find-sub@test.com");

        Course course =
                createCourse("Spring Boot");

        Subscription subscription =
                createSubscription(student, course);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        Optional<Subscription> result =
                subscriptionRepository.findById(
                        savedSubscription.getSubscriptionId()
                );

        assertTrue(result.isPresent());

        assertEquals(
                savedSubscription.getSubscriptionId(),
                result.get().getSubscriptionId()
        );

        assertEquals(
                student.getStudentId(),
                result.get().getStudent().getStudentId()
        );

        assertEquals(
                course.getCourseId(),
                result.get().getCourse().getCourseId()
        );
    }


    @Test
    void testFindSubscriptionByIdNotFound() {

        Optional<Subscription> result =
                subscriptionRepository.findById(99999L);

        assertFalse(result.isPresent());
    }


    @Test
    void testUpdateSubscription() {

        Student student =
                createStudent("update-sub@test.com");

        Course course =
                createCourse("Java");

        Subscription subscription =
                createSubscription(student, course);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        savedSubscription.setStatus(
                SubscriptionStatus.EXPIRED
        );

        savedSubscription.setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        Subscription updatedSubscription =
                subscriptionRepository.save(savedSubscription);

        assertEquals(
                SubscriptionStatus.EXPIRED,
                updatedSubscription.getStatus()
        );

        assertEquals(
                PaymentStatus.REFUNDED,
                updatedSubscription.getPaymentStatus()
        );
    }


    @Test
    void testDeleteSubscription() {

        Student student =
                createStudent("delete-sub@test.com");

        Course course =
                createCourse("Java");

        Subscription subscription =
                createSubscription(student, course);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        Long subscriptionId =
                savedSubscription.getSubscriptionId();

        subscriptionRepository.delete(savedSubscription);

        Optional<Subscription> result =
                subscriptionRepository.findById(subscriptionId);

        assertFalse(result.isPresent());
    }


    @Test
    void testExistsByStudentAndCourseAndStatus() {

        Student student =
                createStudent("duplicate-sub@test.com");

        Course course =
                createCourse("Java Full Stack");

        Subscription subscription =
                createSubscription(student, course);

        subscriptionRepository.save(subscription);

        boolean exists =
                subscriptionRepository
                        .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                                student.getStudentId(),
                                course.getCourseId(),
                                SubscriptionStatus.ACTIVE
                        );

        assertTrue(exists);
    }


    private Student createStudent(String email) {

        Student student = new Student();

        student.setStudentName("Test Student");
        student.setEmail(email);
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        return studentRepository.save(student);
    }


    private Course createCourse(String courseName) {

        Course course = new Course();

        course.setCourseName(courseName);
        course.setDescription("Test Course");
        course.setPrice(15000.0);
        course.setDuration("6 Months");

        return courseRepository.save(course);
    }


    private Subscription createSubscription(
            Student student,
            Course course) {

        Subscription subscription =
                new Subscription();

        subscription.setStudent(student);
        subscription.setCourse(course);

        LocalDate startDate =
                LocalDate.now();

        subscription.setStartDate(startDate);

        subscription.setEndDate(
                startDate.plusMonths(6)
        );

        subscription.setStatus(
                SubscriptionStatus.ACTIVE
        );

        subscription.setPaymentStatus(
                PaymentStatus.PAID
        );

        subscription.setAmount(
                course.getPrice()
        );

        return subscription;
    }
}