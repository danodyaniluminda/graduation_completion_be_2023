package com.ousl.graduation_completion.service;

import java.util.HashMap;
import java.util.List;

public interface OptionalCourseCreditCheckerService {
    List<?> checkCoursesNeedToBeConvertedLevelThree(Integer programId);
    HashMap<String, Object> checkOpenElectiveCourseLevelThree(Long programId);

}
