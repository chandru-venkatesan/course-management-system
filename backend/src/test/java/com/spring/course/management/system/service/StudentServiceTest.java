package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.StudentRequest;
import com.spring.course.management.system.dto.StudentResponse;
import com.spring.course.management.system.exception.StudentNotFoundException;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.repository.StudentRepository;

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
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;


    // ---------------------------------------------------------
    // 1. Test Get All Students
    // ---------------------------------------------------------

    @Test
    void testGetAllStudents() {

        Student student1 = new Student();
        student1.setStudentId(1L);
        student1.setStudentName("Chandru");
        student1.setEmail("chandru@gmail.com");
        student1.setPhoneNumber("9876543210");
        student1.setDateOfBirth("16-08-2000");

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setStudentName("Rahul");
        student2.setEmail("rahul@gmail.com");
        student2.setPhoneNumber("9876543211");
        student2.setDateOfBirth("10-05-2001");

        when(studentRepository.findAll())
                .thenReturn(List.of(student1, student2));

        List<StudentResponse> result =
                studentService.getAllStudents();

        assertEquals(2, result.size());

        assertEquals(
                "Chandru",
                result.get(0).getStudentName()
        );

        assertEquals(
                "Rahul",
                result.get(1).getStudentName()
        );

        verify(studentRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 2. Test Get Student By ID
    // ---------------------------------------------------------

    @Test
    void testGetStudentById() {

        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");
        student.setEmail("chandru@gmail.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        StudentResponse result =
                studentService.getStudentById(1L);

        assertEquals(1L, result.getStudentId());

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                "chandru@gmail.com",
                result.getEmail()
        );

        verify(studentRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 3. Test Get Student By ID - Not Found
    // ---------------------------------------------------------

    @Test
    void testGetStudentByIdStudentNotFound() {

        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudentById(999L)
        );

        verify(studentRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 4. Test Create Student
    // ---------------------------------------------------------

    @Test
    void testCreateStudent() {

        StudentRequest request = new StudentRequest();

        request.setStudentName("Chandru");
        request.setEmail("chandru@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDateOfBirth("16-08-2000");

        Student savedStudent = new Student();

        savedStudent.setStudentId(1L);
        savedStudent.setStudentName("Chandru");
        savedStudent.setEmail("chandru@gmail.com");
        savedStudent.setPhoneNumber("9876543210");
        savedStudent.setDateOfBirth("16-08-2000");

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        StudentResponse result =
                studentService.createStudent(request);

        assertEquals(
                1L,
                result.getStudentId()
        );

        assertEquals(
                "Chandru",
                result.getStudentName()
        );

        assertEquals(
                "chandru@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "9876543210",
                result.getPhoneNumber()
        );

        assertEquals(
                "16-08-2000",
                result.getDateOfBirth()
        );

        verify(studentRepository, times(1))
                .save(any(Student.class));
    }


    // ---------------------------------------------------------
    // 5. Test Update Student
    // ---------------------------------------------------------

    @Test
    void testUpdateStudent() {

        StudentRequest request = new StudentRequest();

        request.setStudentName("Chandru Updated");
        request.setEmail("chandru.updated@gmail.com");
        request.setPhoneNumber("9999999999");
        request.setDateOfBirth("16-08-2000");

        Student existingStudent = new Student();

        existingStudent.setStudentId(1L);
        existingStudent.setStudentName("Chandru");
        existingStudent.setEmail("chandru@gmail.com");
        existingStudent.setPhoneNumber("9876543210");
        existingStudent.setDateOfBirth("16-08-2000");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(existingStudent);

        StudentResponse result =
                studentService.updateStudent(1L, request);

        assertEquals(
                1L,
                result.getStudentId()
        );

        assertEquals(
                "Chandru Updated",
                result.getStudentName()
        );

        assertEquals(
                "chandru.updated@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "9999999999",
                result.getPhoneNumber()
        );

        assertEquals(
                "16-08-2000",
                result.getDateOfBirth()
        );

        verify(studentRepository, times(1))
                .findById(1L);

        verify(studentRepository, times(1))
                .save(existingStudent);
    }


    // ---------------------------------------------------------
    // 6. Test Update Student - Not Found
    // ---------------------------------------------------------

    @Test
    void testUpdateStudentStudentNotFound() {

        StudentRequest request = new StudentRequest();

        request.setStudentName("Chandru");
        request.setEmail("chandru@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setDateOfBirth("16-08-2000");

        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.updateStudent(999L, request)
        );

        verify(studentRepository, times(1))
                .findById(999L);

        verify(studentRepository, never())
                .save(any(Student.class));
    }


    // ---------------------------------------------------------
    // 7. Test Delete Student
    // ---------------------------------------------------------

    @Test
    void testDeleteStudent() {

        Student student = new Student();

        student.setStudentId(1L);
        student.setStudentName("Chandru");
        student.setEmail("chandru@gmail.com");
        student.setPhoneNumber("9876543210");
        student.setDateOfBirth("16-08-2000");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        doNothing()
                .when(studentRepository)
                .delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1))
                .findById(1L);

        verify(studentRepository, times(1))
                .delete(student);
    }


    // ---------------------------------------------------------
    // 8. Test Delete Student - Not Found
    // ---------------------------------------------------------

    @Test
    void testDeleteStudentStudentNotFound() {

        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(999L)
        );

        verify(studentRepository, times(1))
                .findById(999L);

        verify(studentRepository, never())
                .delete(any(Student.class));
    }
}