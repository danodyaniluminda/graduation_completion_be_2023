package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_type")
    private Long courseType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_map_id")
    private Grade grade;
//    @Column(name = "grade_point_id")
//    private String grade;

    @Column(name = "level")
    private Integer level;

    @Column(name = "gpv")
    private Double gpv;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "valid")
    private Boolean valid;

}