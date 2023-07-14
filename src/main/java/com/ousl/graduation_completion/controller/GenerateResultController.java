package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/generateResult")
public class GenerateResultController {
    @Autowired
    ProgramRepository programRepository;
    @GetMapping("/getAllProgrammes")
    public List<Program> getAllProgramme(){
        List<Program> programs = programRepository.findAll();
        return programs;
    }
}
