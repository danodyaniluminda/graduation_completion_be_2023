package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.models.ProgramCriterion;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.service.impl.GenerateResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/generateResult")
public class GenerateResultController {
    @Autowired
    GenerateResultServiceImpl generateResultService;
    @Autowired
    ProgramRepository programRepository;
    @GetMapping("/getAllProgrammes")
    public List<Program> getAllProgramme(){
        List<Program> programs = programRepository.findAll();
        return programs;
    }

    @GetMapping("/getCritriaByProgrammeID")
    public List<ProgramCriterion> getCritriaByProgrammeID(@RequestParam("id") Long progid) {
        List<ProgramCriterion> programCriterionList = generateResultService.getCritriaByProgrammeID(progid);
        return programCriterionList;
    }
}
