package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_type")
    private Long courseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_map_id")
    private Grade grade;

    @Column(name = "level")
    private Integer level;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "gpv")
    private Double gpv;

//    @Column(name = "create_date")
//    private Instant createDate;

}