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
@Table(name = "subject_type")
public class SubjectType {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

}