package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.ALLTables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<ALLTables, Long> {

}
