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
@Table(name = "completion_module_tables")
public class ALLTables {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "table_name")
    private String name;
}
