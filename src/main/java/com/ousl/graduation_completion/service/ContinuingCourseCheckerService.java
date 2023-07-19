package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.dto.DataObject;

public interface ContinuingCourseCheckerService {
    DataObject checkCntCourse(Integer progid);

    int updateFailedOrPassedCritiaStudent(Integer progid);
}
