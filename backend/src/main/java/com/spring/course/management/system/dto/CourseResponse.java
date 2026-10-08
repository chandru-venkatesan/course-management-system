package com.spring.course.management.system.dto;

public class CourseResponse {

    private Long courseId;

    private String courseName;

    private String description;

    private double price;

    private String duration;

    private Long trainerId;

    private String trainerName;

    private Long platformId;

    private String platformName;

    public CourseResponse() {
    }

    public CourseResponse(
            Long courseId,
            String courseName,
            String description,
            double price,
            String duration,
            Long trainerId,
            String trainerName,
            Long platformId,
            String platformName) {

        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.platformId = platformId;
        this.platformName = platformName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

}