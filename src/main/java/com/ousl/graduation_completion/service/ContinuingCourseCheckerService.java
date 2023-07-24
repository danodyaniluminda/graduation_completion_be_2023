package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.dto.DataObject;

public interface ContinuingCourseCheckerService {
    DataObject checkCntCourse(Integer progid);

    DataObject updateFailedOrPassedCritiaStudent(Integer progid);
}
