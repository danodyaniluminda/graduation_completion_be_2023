package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.Student;

import java.util.List;

public interface StudentService {
    List<Student> FindStudentByApplicationId(Long applicationId);

}
