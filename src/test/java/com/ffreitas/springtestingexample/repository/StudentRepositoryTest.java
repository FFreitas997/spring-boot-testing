package com.ffreitas.springtestingexample.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreitas.springtestingexample.entity.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    @Value("classpath:test-data.json")
    private Resource resource;

    private List<Student> savedStudentsTest = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Student> studentsTestList = objectMapper
                .readValue(resource.getInputStream(), new TypeReference<>() {
                });

        if (studentsTestList == null || studentsTestList.isEmpty())
            throw new RuntimeException("No data found in the test-data.json file");

        this.savedStudentsTest = repository.saveAll(studentsTestList);
    }

    @AfterEach
    void tearDown() { repository.deleteAll(); }

    @Test
    @Order(1)
    @DisplayName("Test If Students Are Saved")
    void test_students_are_saved() {
        Assertions.assertThat(savedStudentsTest).isNotNull();
        Assertions.assertThat(savedStudentsTest).isNotEmpty();
        Assertions.assertThat(savedStudentsTest).hasSize(2);
    }

    @Test
    @Order(2)
    @DisplayName("Create Student Repository Test")
    void exists_by_email() {
        var request = savedStudentsTest
                .getFirst()
                .getEmail();

        var result = repository.existsByEmail(request);

        Assertions.assertThat(result).isTrue();

    }

    @Test
    @Order(3)
    @DisplayName("Find Student Repository Test")
    void find_by_email() {
        var request = savedStudentsTest
                .getFirst()
                .getEmail();

        var result = repository
                .findByEmail(request)
                .isPresent();

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("Find All Students Repository Test")
    void find_all() {
        var result = repository.findAll();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(2);
    }
}