package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student_failed_criteria_detail")
public class StudentFailedCriteriaDetail {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "student_id")
    private Long student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    private Criterion criteria;

    @Column(name = "status", length = 4)
    private String status;

    @Column(name = "details")
    private String details;

    @Column(name = "create_date")
    private Instant createDate;

}