package com.ffreitas.springtestingexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreitas.springtestingexample.dto.StudentRequest;
import com.ffreitas.springtestingexample.dto.StudentResponse;
import com.ffreitas.springtestingexample.entity.Student;
import com.ffreitas.springtestingexample.service.StudentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * JDBC Template is used to interact with the SQL database using the JDBC API, or we can use Mongo Template to interact with MongoDB.
 * <pre> {@code
 * @Autowired
 * private JdbcTemplate jdbcTemplate;
 *
 * @Autowired
 * private MongoTemplate mongoTemplate;
 * }</pre>
 * <br><br>We can make http requests to the application using:
 * <ul>
 *     <li>MockMvc</li>
 *     <li>RestTemplate</li>
 *     <li>RestAssured</li>
 * </ul>
 */

//@TestPropertySource("classpath:application.yml")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("HTTP Request - Create Student")
    void http_request_create_student() throws Exception {
        StudentRequest studentRequest = StudentRequest.builder()
                .firstName("Person1")
                .lastName("PersonTest")
                .email("test@gmail.com")
                .build();

        StudentResponse studentResponse = StudentResponse.builder()
                .id("123456789")
                .firstName(studentRequest.firstName())
                .lastName(studentRequest.lastName())
                .email(studentRequest.email())
                .build();

        when(service.createStudent(studentRequest))
                .thenReturn(studentResponse);

        var requestContent = objectMapper.writeValueAsString(studentRequest);

        var response = mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent));

        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("HTTP Request - Get Student by ID ")
    void getStudent() throws Exception {
        Student student = Student.builder()
                .id("123456789")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        StudentResponse responseStudent = StudentResponse.builder()
                .id("123456789")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        when(service.getStudent(student.getId()))
                .thenReturn(responseStudent);

        var response = mockMvc.perform(get("/api/v1/students/{student-id}", student.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(student.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(student.getLastName()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));
    }

    @Test
    @Order(3)
    @DisplayName("HTTP Request - Get All Students")
    void getAllStudents() throws Exception {
        List<StudentResponse> responseList = List.of(
                StudentResponse.builder()
                        .id("123456789")
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@gmail.com")
                        .build(),
                StudentResponse.builder()
                        .id("987654321")
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane.doe@gmail.com")
                        .build()
        );

        when(service.getAllStudents())
                .thenReturn(responseList);

        var response = mockMvc.perform(get("/api/v1/students"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("HTTP Request - Delete Student")
    void deleteStudent() throws Exception {
        doNothing()
                .when(service)
                .deleteStudent(anyString());

        var response = mockMvc.perform(delete("/api/v1/students/{student-id}", "123456789"));

        response.andDo(print())
                .andExpect(status().isAccepted());
    }
}