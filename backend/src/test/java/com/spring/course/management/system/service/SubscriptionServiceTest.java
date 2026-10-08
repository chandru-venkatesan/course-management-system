package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.SubscriptionRequest;
import com.spring.course.management.system.dto.SubscriptionResponse;
import com.spring.course.management.system.exception.CourseNotFoundException;
import com.spring.course.management.system.exception.StudentNotFoundException;
import com.spring.course.management.system.exception.SubscriptionNotFoundException;
import com.spring.course.management.system.model.Course;
import com.spring.course.management.system.model.PaymentStatus;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.model.Subscription;
import com.spring.course.management.system.model.SubscriptionStatus;
import com.spring.course.management.system.repository.CourseRepository;
import com.spring.course.management.system.repository.StudentRepository;
import com.spring.course.management.system.repository.SubscriptionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;


    // ---------------------------------------------------------
    // 1. GET ALL SUBSCRIPTIONS
    // ---------------------------------------------------------

    @Test
    void testGetAllSubscriptions() {

        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentName("Chandru");

        Course course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");

        Subscription subscription = new Subscription();

        subscription.setSubscriptionId(1L);
        subscription.setStudent(student);
        subscription.setCourse(course);
        subscription.setStartDate(LocalDate.of(2026, 1, 1));
        subscription.setEndDate(LocalDate.of(2026, 7, 1));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setPaymentStatus(PaymentStatus.PAID);
        subscription.setAmount(15000.0);

        when(subscriptionRepository.findAll())
                .thenReturn(List.of(subscription));

        List<SubscriptionResponse> result =
                subscriptionService.getAllSubscriptions();

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getSubscriptionId()
        );

        assertEquals(
                "Chandru",
                result.get(0).getStudentName()
        );

        assertEquals(
                "Java Full Stack",
                result.get(0).getCourseName()
        );

        verify(subscriptionRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 2. GET SUBSCRIPTION BY ID
    // ---------------------------------------------------------

    @Test
    void testGetSubscriptionById() {

        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentName("Chandru");

        Course course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");

        Subscription subscription = new Subscription();

        subscription.setSubscriptionId(1L);
        subscription.setStudent(student);
        subscription.setCourse(course);
        subscription.setStartDate(LocalDate.of(2026, 1, 1));
        subscription.setEndDate(LocalDate.of(2026, 7, 1));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setPaymentStatus(PaymentStatus.PAID);
        subscription.setAmount(15000.0);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));

        SubscriptionResponse result =
                subscriptionService.getSubscriptionById(1L);

        assertEquals(
                1L,
                result.getSubscriptionId()
        );

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                "Java Full Stack",
                result.getCourseName()
        );

        assertEquals(
                SubscriptionStatus.ACTIVE,
                result.getStatus()
        );

        assertEquals(
                PaymentStatus.PAID,
                result.getPaymentStatus()
        );

        assertEquals(
                15000.0,
                result.getAmount()
        );

        verify(subscriptionRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 3. SUBSCRIPTION NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testGetSubscriptionByIdSubscriptionNotFound() {

        when(subscriptionRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubscriptionNotFoundException.class,
                () -> subscriptionService.getSubscriptionById(999L)
        );

        verify(subscriptionRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 4. CREATE SUBSCRIPTION
    // ---------------------------------------------------------

    @Test
    void testCreateSubscription() {

        SubscriptionRequest request =
                new SubscriptionRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");
        course.setPrice(15000.0);


        Subscription savedSubscription =
                new Subscription();

        savedSubscription.setSubscriptionId(1L);
        savedSubscription.setStudent(student);
        savedSubscription.setCourse(course);
        savedSubscription.setStartDate(LocalDate.now());
        savedSubscription.setEndDate(
                LocalDate.now().plusMonths(6)
        );
        savedSubscription.setStatus(
                SubscriptionStatus.ACTIVE
        );
        savedSubscription.setPaymentStatus(
                PaymentStatus.PAID
        );
        savedSubscription.setAmount(15000.0);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(subscriptionRepository
                .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                        1L,
                        1L,
                        SubscriptionStatus.ACTIVE
                ))
                .thenReturn(false);

        when(subscriptionRepository.save(any(Subscription.class)))
                .thenReturn(savedSubscription);


        SubscriptionResponse result =
                subscriptionService.createSubscription(request);


        assertEquals(
                1L,
                result.getSubscriptionId()
        );

        assertEquals(
                1L,
                result.getStudentId()
        );

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                1L,
                result.getCourseId()
        );

        assertEquals(
                "Java Full Stack",
                result.getCourseName()
        );

        assertEquals(
                SubscriptionStatus.ACTIVE,
                result.getStatus()
        );

        assertEquals(
                PaymentStatus.PAID,
                result.getPaymentStatus()
        );

        assertEquals(
                15000.0,
                result.getAmount()
        );


        verify(studentRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .findById(1L);

        verify(subscriptionRepository, times(1))
                .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                        1L,
                        1L,
                        SubscriptionStatus.ACTIVE
                );

        verify(subscriptionRepository, times(1))
                .save(any(Subscription.class));
    }


    // ---------------------------------------------------------
    // 5. STUDENT NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testCreateSubscriptionStudentNotFound() {

        SubscriptionRequest request =
                new SubscriptionRequest();

        request.setStudentId(999L);
        request.setCourseId(1L);


        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                StudentNotFoundException.class,
                () -> subscriptionService.createSubscription(request)
        );


        verify(studentRepository, times(1))
                .findById(999L);

        verify(courseRepository, never())
                .findById(anyLong());

        verify(subscriptionRepository, never())
                .save(any(Subscription.class));
    }


    // ---------------------------------------------------------
    // 6. COURSE NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testCreateSubscriptionCourseNotFound() {

        SubscriptionRequest request =
                new SubscriptionRequest();

        request.setStudentId(1L);
        request.setCourseId(999L);


        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                CourseNotFoundException.class,
                () -> subscriptionService.createSubscription(request)
        );


        verify(studentRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .findById(999L);

        verify(subscriptionRepository, never())
                .save(any(Subscription.class));
    }


    // ---------------------------------------------------------
    // 7. DUPLICATE ACTIVE SUBSCRIPTION
    // ---------------------------------------------------------

    @Test
    void testCreateSubscriptionAlreadySubscribed() {

        SubscriptionRequest request =
                new SubscriptionRequest();

        request.setStudentId(1L);
        request.setCourseId(1L);


        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");


        Course course = new Course();

        course.setCourseId(1L);
        course.setCourseName("Java Full Stack");
        course.setPrice(15000.0);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(subscriptionRepository
                .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                        1L,
                        1L,
                        SubscriptionStatus.ACTIVE
                ))
                .thenReturn(true);


        assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.createSubscription(request)
        );


        verify(studentRepository, times(1))
                .findById(1L);

        verify(courseRepository, times(1))
                .findById(1L);

        verify(subscriptionRepository, times(1))
                .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                        1L,
                        1L,
                        SubscriptionStatus.ACTIVE
                );

        verify(subscriptionRepository, never())
                .save(any(Subscription.class));
    }


    // ---------------------------------------------------------
    // 8. DELETE SUBSCRIPTION
    // ---------------------------------------------------------

    @Test
    void testDeleteSubscription() {

        Subscription subscription =
                new Subscription();

        subscription.setSubscriptionId(1L);


        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));


        doNothing()
                .when(subscriptionRepository)
                .delete(subscription);


        subscriptionService.deleteSubscription(1L);


        verify(subscriptionRepository, times(1))
                .findById(1L);

        verify(subscriptionRepository, times(1))
                .delete(subscription);
    }


    // ---------------------------------------------------------
    // 9. DELETE SUBSCRIPTION NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testDeleteSubscriptionSubscriptionNotFound() {

        when(subscriptionRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                SubscriptionNotFoundException.class,
                () -> subscriptionService.deleteSubscription(999L)
        );


        verify(subscriptionRepository, times(1))
                .findById(999L);

        verify(subscriptionRepository, never())
                .delete(any(Subscription.class));
    }
}