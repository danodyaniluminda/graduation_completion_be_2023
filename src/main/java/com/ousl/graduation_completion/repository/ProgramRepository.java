package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}