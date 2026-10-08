package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveStudent() {

        Student student = new Student();

        student.setStudentName("Chandru");
        student.setEmail("chandru@test.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        Student savedStudent = studentRepository.save(student);

        assertNotNull(savedStudent.getStudentId());
        assertEquals("Chandru", savedStudent.getStudentName());
        assertEquals("chandru@test.com", savedStudent.getEmail());
    }

    @Test
    void testFindAllStudents() {

        Student student1 = new Student();

        student1.setStudentName("Student One");
        student1.setEmail("student1@test.com");
        student1.setPhoneNumber("9876543210");
        student1.setDateOfBirth("01-01-2000");


        Student student2 = new Student();

        student2.setStudentName("Student Two");
        student2.setEmail("student2@test.com");
        student2.setPhoneNumber("9876543211");
        student2.setDateOfBirth("02-02-2000");


        Student savedStudent1 = studentRepository.save(student1);
        Student savedStudent2 = studentRepository.save(student2);

        List<Student> students = studentRepository.findAll();

        assertTrue(students.size() >= 2);

        assertTrue(
                students.stream()
                        .anyMatch(student ->
                                student.getStudentId()
                                        .equals(savedStudent1.getStudentId()))
        );

        assertTrue(
                students.stream()
                        .anyMatch(student ->
                                student.getStudentId()
                                        .equals(savedStudent2.getStudentId()))
        );
    }

    @Test
    void testFindStudentById() {

        Student student = new Student();

        student.setStudentName("Chandru");
        student.setEmail("chandru2@test.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        Student savedStudent = studentRepository.save(student);

        Optional<Student> result =
                studentRepository.findById(savedStudent.getStudentId());

        assertTrue(result.isPresent());
        assertEquals("Chandru", result.get().getStudentName());
        assertEquals("chandru2@test.com", result.get().getEmail());
    }

    @Test
    void testFindStudentByIdNotFound() {

        Optional<Student> result =
                studentRepository.findById(99999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateStudent() {

        Student student = new Student();

        student.setStudentName("Chandru");
        student.setEmail("update@test.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        Student savedStudent = studentRepository.save(student);

        savedStudent.setStudentName("Chandru Updated");
        savedStudent.setPhoneNumber("9999999999");

        Student updatedStudent =
                studentRepository.save(savedStudent);

        assertEquals(
                "Chandru Updated",
                updatedStudent.getStudentName()
        );

        assertEquals(
                "9999999999",
                updatedStudent.getPhoneNumber()
        );
    }

    @Test
    void testDeleteStudent() {

        Student student = new Student();

        student.setStudentName("Delete Student");
        student.setEmail("delete@test.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        Student savedStudent = studentRepository.save(student);

        Long studentId = savedStudent.getStudentId();

        studentRepository.delete(savedStudent);

        Optional<Student> result =
                studentRepository.findById(studentId);

        assertFalse(result.isPresent());
    }
}