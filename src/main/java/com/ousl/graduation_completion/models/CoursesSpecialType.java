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
@Table(name = "courses_special_type")
public class CoursesSpecialType {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cst_description")
    private String cstDescription;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "archive", nullable = false)
    private Boolean archive = false;

    @Column(name = "created_by")
    private Long createdBy;

}