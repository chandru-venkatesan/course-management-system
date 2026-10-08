package com.spring.course.management.system.dto;

import com.spring.course.management.system.model.PaymentStatus;
import com.spring.course.management.system.model.SubscriptionStatus;

import java.time.LocalDate;

public class SubscriptionResponse {

    private Long subscriptionId;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private LocalDate startDate;
    private LocalDate endDate;

    private SubscriptionStatus status;
    private PaymentStatus paymentStatus;

    private Double amount;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(
            Long subscriptionId,
            Long studentId,
            String studentName,
            Long courseId,
            String courseName,
            LocalDate startDate,
            LocalDate endDate,
            SubscriptionStatus status,
            PaymentStatus paymentStatus,
            Double amount) {

        this.subscriptionId = subscriptionId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}