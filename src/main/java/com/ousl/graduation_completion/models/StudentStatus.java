package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "student_status")
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "student_id")
    private Long student;

    @Column(name = "program_criteria_id")
    private Long programCriterion;

    @Column(name = "create_date")
    private LocalDateTime createDate;

}