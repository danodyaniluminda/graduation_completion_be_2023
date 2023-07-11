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
@Table(name = "course_type")
public class CourseType {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "course_type_name")
    private Integer courseTypeName;

}