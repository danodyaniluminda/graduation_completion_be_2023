package com.ousl.graduation_completion.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "program")
public class Program {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "academic_duration")
    private String academicDuration;

    @Column(name = "program_of_study")
    private Integer programOfStudy;

}