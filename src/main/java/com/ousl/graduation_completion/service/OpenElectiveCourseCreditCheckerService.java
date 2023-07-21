package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.dto.DataObject;

import java.util.HashMap;

public interface OpenElectiveCourseCreditCheckerService {
    DataObject checkCoursesNeedToBeConverted(Integer programId, Integer level);
    HashMap<String, Object> checkOpenElectiveCourseLevelThree(Long programId);
    HashMap<String, Object> checkOpenElectiveCourseLevelFive(Long programId);

}
