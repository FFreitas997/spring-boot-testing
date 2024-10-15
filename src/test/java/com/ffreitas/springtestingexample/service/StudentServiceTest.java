package com.ffreitas.springtestingexample.service;

import com.ffreitas.springtestingexample.dto.StudentRequest;
import com.ffreitas.springtestingexample.dto.StudentResponse;
import com.ffreitas.springtestingexample.entity.Student;
import com.ffreitas.springtestingexample.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Student Service Test
 * <p>
 * This class is responsible for testing the Student Service methods
 * </p>
 *   <ul>
 *       <li>Create Student</li>
 *       <li>Get Student</li>
 *       <li>Get All Students</li>
 *       <li>Delete Student</li>
 *   </ul>
 * <b>Important Note</b>: For testing purposes, we could use an in-memory database like H2, but for this example, we are using the MongoDB database.
 */

//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    @Order(1)
    @DisplayName("Create Student Service Test")
    public void create_student() {
        var request = StudentRequest.builder()
                .id(null)
                .email("John")
                .firstName("Doe")
                .lastName("john.doe@gmail.com")
                .build();

        var response = Student.builder()
                .id("123456789")
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        when(repository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        when(repository.save(Mockito.any(Student.class)))
                .thenReturn(response);

        var result = service.createStudent(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(response.getEmail(), result.email());
        assertEquals(response.getFirstName(), result.firstName());
        assertEquals(response.getLastName(), result.lastName());

        verify(repository, times(1)).existsByEmail(Mockito.anyString());
        verify(repository, times(1)).save(Mockito.any(Student.class));
    }

    @Test
    @Order(2)
    @DisplayName("Create Student Service Test - Parameter is null")
    public void create_student_if_parameter_is_null() {
        var expNull = assertThrows(IllegalArgumentException.class, () -> service.createStudent(null));
        assertEquals("Student cannot be null", expNull.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Create Student Service Test - Student already exists")
    public void create_student_if_student_already_exists() {
        var request = StudentRequest.builder()
                .id(null)
                .email("John")
                .firstName("Doe")
                .lastName("john.doe@gmail.com")
                .build();

        when(repository.existsByEmail(request.email()))
                .thenReturn(true);

        var exp = assertThrows(IllegalArgumentException.class, () -> service.createStudent(request));
        assertEquals("Student with email " + request.email() + " already exists", exp.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Get Student Service Test")
    void get_student() {
        var request = "123456789";
        var response = Student.builder()
                .id("123456789")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        when(repository.findById(request))
                .thenReturn(Optional.of(response));

        var actualStudent = service.getStudent(request);

        assertNotNull(actualStudent);
        assertEquals(response.getId(), actualStudent.id());
        assertEquals(response.getFirstName(), actualStudent.firstName());
        assertEquals(response.getLastName(), actualStudent.lastName());
        assertEquals(response.getEmail(), actualStudent.email());

        verify(repository, times(1)).findById(request);
    }

    @Test
    @Order(5)
    @DisplayName("Get Student Service Test - Parameter is null")
    void get_student_if_parameter_is_null() {
        when(repository.findById(null))
                .thenThrow(new IllegalArgumentException("Student with id " + null + " not found"));

        assertThrows(IllegalArgumentException.class, () -> service.getStudent(null));

        verify(repository, times(1)).findById(null);
    }

    @Test
    @Order(6)
    @DisplayName("Get All Students Service Test")
    void get_all_students() {
        var responseList = List.of(
                Student.builder()
                        .id("123456789")
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@gmail.com")
                        .build()
        );

        when(repository.findAll())
                .thenReturn(responseList);

        List<StudentResponse> result = service.getAllStudents();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(responseList.size(), result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    @Order(7)
    @DisplayName("Delete Student Service Test")
    void delete_student() {
        var request = "123456789";

        doNothing().when(repository).deleteById(request);

        service.deleteStudent(request);

        verify(repository, times(1)).deleteById(request);
    }
}