package com.ffreitas.springtestingexample.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record StudentResponse(

        String id,

        String firstName,

        String lastName,

        String email

) implements Serializable {
}
