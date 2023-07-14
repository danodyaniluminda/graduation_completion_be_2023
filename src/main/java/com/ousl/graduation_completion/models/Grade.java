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
@Table(name = "grade")
public class Grade {
    @Id
    @Column(name = "map_value", nullable = false)
    private Long id;

    @Column(name = "garde")
    private String garde;

    @Column(name = "gpv")
    private Double gpv;

}