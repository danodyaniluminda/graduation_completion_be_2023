package com.ousl.graduation_completion.service;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

public interface RegularCourseCreditCheckerService {
    List<String> checkCoursesNeedToBeConverted(Long programId);
    HashMap<String, Object> checkRegularCourseCredits(Long programId,Integer level, Integer noOfCreditsRequired);
    public HashMap<String, Object> checkS1RegularCourseCredits(Long programId,Integer level);
}
