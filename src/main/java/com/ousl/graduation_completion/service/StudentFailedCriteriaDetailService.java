package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.StudentFailedCriteriaDetail;

import java.util.List;

public interface StudentFailedCriteriaDetailService {

    List<StudentFailedCriteriaDetail> findStudentFailedCriteriaDetails(Long student);
}
