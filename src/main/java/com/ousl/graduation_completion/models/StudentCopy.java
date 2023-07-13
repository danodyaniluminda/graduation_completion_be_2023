package com.ousl.graduation_completion.models;

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
@Table(name = "student_copy")
public class StudentCopy {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_type")
    private Long courseType;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "level")
    private Integer level;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "grade_point_id")
    private Integer gradePointId;

}