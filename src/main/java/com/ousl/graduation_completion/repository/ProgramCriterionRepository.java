package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.models.ProgramCriterion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramCriterionRepository extends JpaRepository<ProgramCriterion, Long> {

    List<ProgramCriterion> getAllByProgramAndActive(Program program,boolean b);
    ProgramCriterion getProgramCriterionByProgramAndActive(Program program,Boolean b);

    ProgramCriterion getProgramCriterionBySequenceIdAndProgram(Long l,Program program);
    List<ProgramCriterion> findByProgramIdOrderBySequenceIdAsc(Long id);

    Optional<ProgramCriterion> getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(Long programId, boolean b, Long criteriaId);
}