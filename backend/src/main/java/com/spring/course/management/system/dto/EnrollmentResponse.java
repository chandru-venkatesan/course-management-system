package com.spring.course.management.system.dto;

import com.spring.course.management.system.model.EnrollmentStatus;

import java.time.LocalDate;

public class EnrollmentResponse {

    private Long enrollmentId;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private LocalDate enrollmentDate;

    private EnrollmentStatus status;

    private Integer progress;

    public EnrollmentResponse() {
    }

    public EnrollmentResponse(
            Long enrollmentId,
            Long studentId,
            String studentName,
            Long courseId,
            String courseName,
            LocalDate enrollmentDate,
            EnrollmentStatus status,
            Integer progress) {

        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.progress = progress;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
