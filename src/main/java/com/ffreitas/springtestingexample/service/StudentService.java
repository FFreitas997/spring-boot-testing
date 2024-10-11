package com.ffreitas.springtestingexample.service;

import com.ffreitas.springtestingexample.dto.StudentRequest;
import com.ffreitas.springtestingexample.dto.StudentResponse;
import com.ffreitas.springtestingexample.entity.Student;
import com.ffreitas.springtestingexample.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentResponse createStudent(StudentRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Student cannot be null");

        boolean hasStudentByEmail = repository.existsByEmail(request.email());

        if (hasStudentByEmail)
            throw new IllegalArgumentException("Student with email " + request.email() + " already exists");

        Student entity = Student
                .builder()
                .id(null)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        Student savedStudent = repository.save(entity);

        return StudentResponse
                .builder()
                .id(savedStudent.getId())
                .firstName(savedStudent.getFirstName())
                .lastName(savedStudent.getLastName())
                .email(savedStudent.getEmail())
                .build();
    }

    public StudentResponse getStudent(String id) {
        return repository
                .findById(id)
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + id + " not found"));
    }

    public List<StudentResponse> getAllStudents() {
        return repository
                .findAll()
                .stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail()
                ))
                .toList();
    }

    public void deleteStudent(String id) {
        repository.deleteById(id);
    }
}
