package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.Student;
import com.ousl.graduation_completion.repository.StudentRepository;
import com.ousl.graduation_completion.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public List<Student> FindStudentByApplicationId(Long applicationId) {
        return studentRepository.findByApplicationId(applicationId);
    }
}
