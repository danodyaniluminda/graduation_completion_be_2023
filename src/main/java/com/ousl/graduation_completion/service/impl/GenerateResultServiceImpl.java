package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.ProgramCriterion;
import com.ousl.graduation_completion.repository.ProgramCriterionRepository;
import com.ousl.graduation_completion.service.GenerateResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateResultServiceImpl implements GenerateResultService {
    @Autowired
    ProgramCriterionRepository programCriterionRepository;
    @Override
    public List<ProgramCriterion> getCritriaByProgrammeID(Long progid) {
        List<ProgramCriterion> programCriterionList = programCriterionRepository.findByProgramIdOrderBySequenceIdAsc(progid);
        return programCriterionList;
    }
}
