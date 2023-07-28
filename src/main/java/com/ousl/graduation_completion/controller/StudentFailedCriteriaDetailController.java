package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.models.StudentFailedCriteriaDetail;
import com.ousl.graduation_completion.service.StudentFailedCriteriaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/student_failed_criteria_details")
public class StudentFailedCriteriaDetailController {

    @Autowired
    StudentFailedCriteriaDetailService studentFailedCriteriaDetailService;

    @GetMapping(value ="/failed_criteria_find_by_application_id")
    public List<StudentFailedCriteriaDetail> getAllFailedCriteriaDetails(@RequestParam("id") Long student){
        return studentFailedCriteriaDetailService.findStudentFailedCriteriaDetails(student);
    }
}
