package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.TrainerRequest;
import com.spring.course.management.system.dto.TrainerResponse;
import com.spring.course.management.system.exception.TrainerNotFoundException;
import com.spring.course.management.system.model.Trainer;
import com.spring.course.management.system.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public TrainerResponse createTrainer(TrainerRequest request) {

        Trainer trainer = new Trainer();

        trainer.setTrainerName(request.getTrainerName());
        trainer.setEmail(request.getEmail());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setExperience(request.getExperience());

        Trainer savedTrainer = trainerRepository.save(trainer);

        return mapToResponse(savedTrainer);
    }

    public List<TrainerResponse> getAllTrainers() {

        return trainerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainerResponse getTrainerById(Long trainerId) {

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() ->
                        new TrainerNotFoundException(
                                "Trainer not found with ID: " + trainerId
                        ));

        return mapToResponse(trainer);
    }

    public TrainerResponse updateTrainer(
            Long trainerId,
            TrainerRequest request) {

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() ->
                        new TrainerNotFoundException(
                                "Trainer not found with ID: " + trainerId
                        ));

        trainer.setTrainerName(request.getTrainerName());
        trainer.setEmail(request.getEmail());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setExperience(request.getExperience());

        Trainer updatedTrainer = trainerRepository.save(trainer);

        return mapToResponse(updatedTrainer);
    }

    public void deleteTrainer(Long trainerId) {

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() ->
                        new TrainerNotFoundException(
                                "Trainer not found with ID: " + trainerId
                        ));

        trainerRepository.delete(trainer);
    }

    private TrainerResponse mapToResponse(Trainer trainer) {

        return new TrainerResponse(
                trainer.getTrainerId(),
                trainer.getTrainerName(),
                trainer.getEmail(),
                trainer.getSpecialization(),
                trainer.getExperience()
        );
    }
}
