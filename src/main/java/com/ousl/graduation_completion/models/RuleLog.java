package com.ousl.graduation_completion.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rule_log")
public class RuleLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    public Program program;

    @Column(name = "student_id")
    public Long student;

    @Column(name = "log")
    public String log;

    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    @Column(name = "create_date")
    public LocalDateTime createDate;

}