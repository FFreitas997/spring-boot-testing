package com.ffreitas.springtestingexample.controller;

import com.ffreitas.springtestingexample.dto.StudentRequest;
import com.ffreitas.springtestingexample.dto.StudentResponse;
import com.ffreitas.springtestingexample.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createStudent(@RequestBody @Valid StudentRequest studentRequest) {
        var response = studentService.createStudent(studentRequest);
        log.info("Student created with ID: {}", response.id());
    }

    @GetMapping("/{student-id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponse getStudent(@PathVariable("student-id") String id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents();
    }

    @DeleteMapping("/{student-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudent(@PathVariable("student-id") String id) {
        studentService.deleteStudent(id);
    }
}
