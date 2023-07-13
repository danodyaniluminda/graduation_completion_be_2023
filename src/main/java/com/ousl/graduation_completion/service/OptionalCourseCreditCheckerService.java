package com.ousl.graduation_completion.service;

import java.util.HashMap;
import java.util.List;

public interface OptionalCourseCreditCheckerService {
    List<Object[]> checkCoursesNeedToBeConverted(Integer programId);
    HashMap<String, Object> checkRegularCourseCredits(Long applicationId, Long programId, Integer level, Integer noOfCreditsRequired);

}
