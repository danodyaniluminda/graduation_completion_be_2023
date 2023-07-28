package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.StudentFailedCriteriaDetail;
import com.ousl.graduation_completion.repository.StudentFailedCriteriaDetailRepository;
import com.ousl.graduation_completion.service.StudentFailedCriteriaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentFailedCriteriaDetailImpl implements StudentFailedCriteriaDetailService {

    @Autowired
    StudentFailedCriteriaDetailRepository studentFailedCriteriaDetailRepository;

    @Override
    public List<StudentFailedCriteriaDetail> findStudentFailedCriteriaDetails(Long student) {
        return studentFailedCriteriaDetailRepository.findByStudent(student);
    }
}
