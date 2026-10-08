package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.TrainerRequest;
import com.spring.course.management.system.dto.TrainerResponse;
import com.spring.course.management.system.exception.TrainerNotFoundException;
import com.spring.course.management.system.model.Trainer;
import com.spring.course.management.system.repository.TrainerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;


    // ---------------------------------------------------------
    // 1. Test Get All Trainers
    // ---------------------------------------------------------

    @Test
    void testGetAllTrainers() {

        Trainer trainer1 = new Trainer();

        trainer1.setTrainerId(1L);
        trainer1.setTrainerName("Arun");
        trainer1.setEmail("arun@gmail.com");
        trainer1.setSpecialization("Java");
        trainer1.setExperience(5);


        Trainer trainer2 = new Trainer();

        trainer2.setTrainerId(2L);
        trainer2.setTrainerName("Kumar");
        trainer2.setEmail("kumar@gmail.com");
        trainer2.setSpecialization("Spring Boot");
        trainer2.setExperience(7);


        when(trainerRepository.findAll())
                .thenReturn(List.of(trainer1, trainer2));


        List<TrainerResponse> result =
                trainerService.getAllTrainers();


        assertEquals(2, result.size());

        assertEquals(
                "Arun",
                result.get(0).getTrainerName()
        );

        assertEquals(
                "Kumar",
                result.get(1).getTrainerName()
        );


        verify(trainerRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 2. Test Get Trainer By ID
    // ---------------------------------------------------------

    @Test
    void testGetTrainerById() {

        Trainer trainer = new Trainer();

        trainer.setTrainerId(1L);
        trainer.setTrainerName("Arun");
        trainer.setEmail("arun@gmail.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);


        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));


        TrainerResponse result =
                trainerService.getTrainerById(1L);


        assertEquals(
                1L,
                result.getTrainerId()
        );

        assertEquals(
                "Arun",
                result.getTrainerName()
        );

        assertEquals(
                "arun@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "Java",
                result.getSpecialization()
        );

        assertEquals(
                5,
                result.getExperience()
        );


        verify(trainerRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 3. Test Get Trainer By ID - Not Found
    // ---------------------------------------------------------

    @Test
    void testGetTrainerByIdTrainerNotFound() {

        when(trainerRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                TrainerNotFoundException.class,
                () -> trainerService.getTrainerById(999L)
        );


        verify(trainerRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 4. Test Create Trainer
    // ---------------------------------------------------------

    @Test
    void testCreateTrainer() {

        TrainerRequest request = new TrainerRequest();

        request.setTrainerName("Arun");
        request.setEmail("arun@gmail.com");
        request.setSpecialization("Java");
        request.setExperience(5);


        Trainer savedTrainer = new Trainer();

        savedTrainer.setTrainerId(1L);
        savedTrainer.setTrainerName("Arun");
        savedTrainer.setEmail("arun@gmail.com");
        savedTrainer.setSpecialization("Java");
        savedTrainer.setExperience(5);


        when(trainerRepository.save(any(Trainer.class)))
                .thenReturn(savedTrainer);


        TrainerResponse result =
                trainerService.createTrainer(request);


        assertEquals(
                1L,
                result.getTrainerId()
        );

        assertEquals(
                "Arun",
                result.getTrainerName()
        );

        assertEquals(
                "arun@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "Java",
                result.getSpecialization()
        );

        assertEquals(
                5,
                result.getExperience()
        );


        verify(trainerRepository, times(1))
                .save(any(Trainer.class));
    }


    // ---------------------------------------------------------
    // 5. Test Update Trainer
    // ---------------------------------------------------------

    @Test
    void testUpdateTrainer() {

        TrainerRequest request = new TrainerRequest();

        request.setTrainerName("Arun Updated");
        request.setEmail("arun.updated@gmail.com");
        request.setSpecialization("Spring Boot");
        request.setExperience(8);


        Trainer existingTrainer = new Trainer();

        existingTrainer.setTrainerId(1L);
        existingTrainer.setTrainerName("Arun");
        existingTrainer.setEmail("arun@gmail.com");
        existingTrainer.setSpecialization("Java");
        existingTrainer.setExperience(5);


        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(existingTrainer));

        when(trainerRepository.save(any(Trainer.class)))
                .thenReturn(existingTrainer);


        TrainerResponse result =
                trainerService.updateTrainer(1L, request);


        assertEquals(
                1L,
                result.getTrainerId()
        );

        assertEquals(
                "Arun Updated",
                result.getTrainerName()
        );

        assertEquals(
                "arun.updated@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "Spring Boot",
                result.getSpecialization()
        );

        assertEquals(
                8,
                result.getExperience()
        );


        verify(trainerRepository, times(1))
                .findById(1L);

        verify(trainerRepository, times(1))
                .save(existingTrainer);
    }


    // ---------------------------------------------------------
    // 6. Test Update Trainer - Not Found
    // ---------------------------------------------------------

    @Test
    void testUpdateTrainerTrainerNotFound() {

        TrainerRequest request = new TrainerRequest();

        request.setTrainerName("Arun");
        request.setEmail("arun@gmail.com");
        request.setSpecialization("Java");
        request.setExperience(5);


        when(trainerRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                TrainerNotFoundException.class,
                () -> trainerService.updateTrainer(999L, request)
        );


        verify(trainerRepository, times(1))
                .findById(999L);

        verify(trainerRepository, never())
                .save(any(Trainer.class));
    }


    // ---------------------------------------------------------
    // 7. Test Delete Trainer
    // ---------------------------------------------------------

    @Test
    void testDeleteTrainer() {

        Trainer trainer = new Trainer();

        trainer.setTrainerId(1L);
        trainer.setTrainerName("Arun");
        trainer.setEmail("arun@gmail.com");
        trainer.setSpecialization("Java");
        trainer.setExperience(5);


        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));


        doNothing()
                .when(trainerRepository)
                .delete(trainer);


        trainerService.deleteTrainer(1L);


        verify(trainerRepository, times(1))
                .findById(1L);

        verify(trainerRepository, times(1))
                .delete(trainer);
    }


    // ---------------------------------------------------------
    // 8. Test Delete Trainer - Not Found
    // ---------------------------------------------------------

    @Test
    void testDeleteTrainerTrainerNotFound() {

        when(trainerRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                TrainerNotFoundException.class,
                () -> trainerService.deleteTrainer(999L)
        );


        verify(trainerRepository, times(1))
                .findById(999L);

        verify(trainerRepository, never())
                .delete(any(Trainer.class));
    }
}