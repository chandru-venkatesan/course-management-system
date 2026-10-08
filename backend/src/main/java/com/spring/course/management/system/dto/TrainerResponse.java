package com.spring.course.management.system.dto;

public class TrainerResponse {

    private Long trainerId;
    private String trainerName;
    private String email;
    private String specialization;
    private Integer experience;

    public TrainerResponse() {
    }

    public TrainerResponse(Long trainerId,
                           String trainerName,
                           String email,
                           String specialization,
                           Integer experience) {

        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.email = email;
        this.specialization = specialization;
        this.experience = experience;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
