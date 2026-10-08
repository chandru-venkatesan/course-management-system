package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.StudentRequest;
import com.spring.course.management.system.dto.StudentResponse;
import com.spring.course.management.system.exception.StudentNotFoundException;
import com.spring.course.management.system.model.Student;
import com.spring.course.management.system.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE
    public StudentResponse createStudent(StudentRequest request) {

        Student student = new Student();

        student.setStudentName(request.getStudentName());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDateOfBirth(request.getDateOfBirth());

        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    // GET ALL
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY ID
    public StudentResponse getStudentById(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + studentId
                        )
                );

        return mapToResponse(student);
    }

    // UPDATE
    public StudentResponse updateStudent(
            Long studentId,
            StudentRequest request) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + studentId
                        )
                );

        student.setStudentName(request.getStudentName());
        student.setEmail(request.getEmail());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setDateOfBirth(request.getDateOfBirth());

        Student updatedStudent =
                studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    // DELETE
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + studentId
                        )
                );

        studentRepository.delete(student);
    }

    // ENTITY → RESPONSE DTO
    private StudentResponse mapToResponse(Student student) {

        return new StudentResponse(
                student.getStudentId(),
                student.getStudentName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getDateOfBirth()
        );
    }
}
