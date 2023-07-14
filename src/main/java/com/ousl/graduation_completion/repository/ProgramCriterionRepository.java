package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.ProgramCriterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProgramCriterionRepository extends JpaRepository<ProgramCriterion, Long> {
    List<ProgramCriterion> findByProgramIdOrderBySequenceIdAsc(Long id);
}