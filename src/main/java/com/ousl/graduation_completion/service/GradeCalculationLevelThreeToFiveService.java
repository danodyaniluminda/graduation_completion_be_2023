package com.ousl.graduation_completion.service;

import java.util.HashMap;

public interface GradeCalculationLevelThreeToFiveService {

    HashMap<String,Object> gradeCalculationLevelThree(Long programId);

    HashMap<String,Object> gradeCalculationLevelFour(Long programId);
    HashMap<String,Object> gradeCalculationLevelFive(Long programId);
}