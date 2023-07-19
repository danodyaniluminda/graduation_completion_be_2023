package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.ProgramCriterion;

import java.util.List;

public interface GenerateResultService {
    List<ProgramCriterion> getCritriaByProgrammeID(Long progid);
}
