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
@Table(name = "criteria")
public class Criterion {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "criteria_name")
    private String criteriaName;

}