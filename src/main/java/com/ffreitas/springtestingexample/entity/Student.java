package com.ffreitas.springtestingexample.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Student {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;
}
