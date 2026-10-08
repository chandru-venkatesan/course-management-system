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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.subscriptionRepository = subscriptionRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public SubscriptionResponse createSubscription(
            SubscriptionRequest request) {

        Student student = studentRepository
                .findById(request.getStudentId())
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: "
                                        + request.getStudentId()
                        ));

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found with ID: "
                                        + request.getCourseId()
                        ));

        boolean alreadySubscribed =
                subscriptionRepository
                        .existsByStudent_StudentIdAndCourse_CourseIdAndStatus(
                                request.getStudentId(),
                                request.getCourseId(),
                                SubscriptionStatus.ACTIVE
                        );

        if (alreadySubscribed) {
            throw new IllegalStateException(
                    "Student already has an active subscription for this course"
            );
        }

        Subscription subscription = new Subscription();

        subscription.setStudent(student);
        subscription.setCourse(course);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(6);

        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);

        subscription.setStatus(SubscriptionStatus.ACTIVE);

         // For now we assume payment is successful.

        subscription.setPaymentStatus(PaymentStatus.PAID);


         // Amount comes from the Course,

        subscription.setAmount(course.getPrice());

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return mapToResponse(savedSubscription);
    }

    public List<SubscriptionResponse> getAllSubscriptions() {

        return subscriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SubscriptionResponse getSubscriptionById(
            Long subscriptionId) {

        Subscription subscription =
                subscriptionRepository.findById(subscriptionId)
                        .orElseThrow(() ->
                                new SubscriptionNotFoundException(
                                        "Subscription not found with ID: "
                                                + subscriptionId
                                ));

        return mapToResponse(subscription);
    }

    public void deleteSubscription(Long subscriptionId) {

        Subscription subscription =
                subscriptionRepository.findById(subscriptionId)
                        .orElseThrow(() ->
                                new SubscriptionNotFoundException(
                                        "Subscription not found with ID: "
                                                + subscriptionId
                                ));

        subscriptionRepository.delete(subscription);
    }

    private SubscriptionResponse mapToResponse(
            Subscription subscription) {

        return new SubscriptionResponse(
                subscription.getSubscriptionId(),

                subscription.getStudent().getStudentId(),
                subscription.getStudent().getStudentName(),

                subscription.getCourse().getCourseId(),
                subscription.getCourse().getCourseName(),

                subscription.getStartDate(),
                subscription.getEndDate(),

                subscription.getStatus(),
                subscription.getPaymentStatus(),

                subscription.getAmount()
        );
    }
}
