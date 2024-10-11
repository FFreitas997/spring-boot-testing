package com.ffreitas.springtestingexample.repository;

import com.ffreitas.springtestingexample.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(@NonNull String email);

    Optional<Student> findByEmail(@NonNull String email);
}
