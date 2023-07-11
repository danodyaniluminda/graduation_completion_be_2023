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
@Table(name = "subject")
public class Subject {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "subject_name")
    private Integer subjectName;

}