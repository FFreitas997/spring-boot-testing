package com.ffreitas.springtestingexample.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record StudentRequest(

        String id,

        @NotNull(message = "Student first name is required")
        @NotEmpty(message = "Student first name is required")
        String firstName,

        @NotNull(message = "Student last name is required")
        @NotEmpty(message = "Student last name is required")
        String lastName,

        @NotNull(message = "Student email is required")
        @NotEmpty(message = "Student email is required")
        @Email(message = "Student email is invalid")
        String email

) implements Serializable { }
