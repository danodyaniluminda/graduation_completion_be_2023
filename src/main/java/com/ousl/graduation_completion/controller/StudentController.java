package com.ousl.graduation_completion.controller;


import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Student;
import com.ousl.graduation_completion.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/student")

public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping(value ="/find_by_application_id")
    public List<Student> getAllRecords(@RequestParam("id") Long applicationId){
        return studentService.FindStudentByApplicationId(applicationId);
    }

}
