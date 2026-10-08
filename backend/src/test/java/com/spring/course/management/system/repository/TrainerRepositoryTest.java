package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Trainer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void testSaveTrainer() {

        Trainer trainer = new Trainer();

        trainer.setTrainerName("John");
        trainer.setEmail("john@test.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);

        Trainer savedTrainer = trainerRepository.save(trainer);

        assertNotNull(savedTrainer.getTrainerId());
        assertEquals("John", savedTrainer.getTrainerName());
        assertEquals("john@test.com", savedTrainer.getEmail());
        assertEquals("Java", savedTrainer.getSpecialization());
        assertEquals(5, savedTrainer.getExperience());
    }

    @Test
    void testFindAllTrainers() {

        Trainer trainer1 = new Trainer();

        trainer1.setTrainerName("John");
        trainer1.setEmail("john1@test.com");
        trainer1.setSpecialization("Java");
        trainer1.setExperience(5);


        Trainer trainer2 = new Trainer();

        trainer2.setTrainerName("David");
        trainer2.setEmail("david@test.com");
        trainer2.setSpecialization("Spring Boot");
        trainer2.setExperience(7);


        Trainer savedTrainer1 = trainerRepository.save(trainer1);
        Trainer savedTrainer2 = trainerRepository.save(trainer2);

        List<Trainer> trainers = trainerRepository.findAll();

        assertTrue(trainers.size() >= 2);

        assertTrue(
                trainers.stream()
                        .anyMatch(trainer ->
                                trainer.getTrainerId()
                                        .equals(savedTrainer1.getTrainerId()))
        );

        assertTrue(
                trainers.stream()
                        .anyMatch(trainer ->
                                trainer.getTrainerId()
                                        .equals(savedTrainer2.getTrainerId()))
        );
    }

    @Test
    void testFindTrainerById() {

        Trainer trainer = new Trainer();

        trainer.setTrainerName("John");
        trainer.setEmail("john2@test.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);

        Trainer savedTrainer = trainerRepository.save(trainer);

        Optional<Trainer> result =
                trainerRepository.findById(savedTrainer.getTrainerId());

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getTrainerName());
        assertEquals("john2@test.com", result.get().getEmail());
        assertEquals("Java", result.get().getSpecialization());
    }

    @Test
    void testFindTrainerByIdNotFound() {

        Optional<Trainer> result =
                trainerRepository.findById(99999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateTrainer() {

        Trainer trainer = new Trainer();

        trainer.setTrainerName("John");
        trainer.setEmail("update@test.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);

        Trainer savedTrainer = trainerRepository.save(trainer);

        savedTrainer.setTrainerName("John Updated");
        savedTrainer.setSpecialization("Advanced Java");
        savedTrainer.setExperience(8);

        Trainer updatedTrainer =
                trainerRepository.save(savedTrainer);

        assertEquals(
                "John Updated",
                updatedTrainer.getTrainerName()
        );

        assertEquals(
                "Advanced Java",
                updatedTrainer.getSpecialization()
        );

        assertEquals(
                8,
                updatedTrainer.getExperience()
        );
    }

    @Test
    void testDeleteTrainer() {

        Trainer trainer = new Trainer();

        trainer.setTrainerName("Delete Trainer");
        trainer.setEmail("delete@test.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);

        Trainer savedTrainer = trainerRepository.save(trainer);

        Long trainerId = savedTrainer.getTrainerId();

        trainerRepository.delete(savedTrainer);

        Optional<Trainer> result =
                trainerRepository.findById(trainerId);

        assertFalse(result.isPresent());
    }
}