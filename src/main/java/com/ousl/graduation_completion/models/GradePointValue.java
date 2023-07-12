package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "grade_point_value")
public class GradePointValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "garde")
    private String garde;

    @Column(name = "garde_point")
    private Double gardePoint;

}