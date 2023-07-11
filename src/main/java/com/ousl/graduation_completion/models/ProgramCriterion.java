package com.ousl.graduation_completion.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "program_criteria")
public class ProgramCriterion {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criteria_id")
    private Criterion criteria;

    @Column(name = "sequence_id")
    private Long sequenceId;

}