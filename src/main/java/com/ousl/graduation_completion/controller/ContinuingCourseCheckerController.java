package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.service.impl.ContinuingCourseCheckerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/graduation-completion/gpa_calculation")
public class ContinuingCourseCheckerController {
    @Autowired
    ContinuingCourseCheckerServiceImpl continuingCourseCheckerService;
    @GetMapping("/checkCntCourse")
    public ResponseEntity<DataObject> checkCntCourse(@RequestParam("id") Integer progid) {
        return ResponseEntity.ok(continuingCourseCheckerService.checkCntCourse(progid));
    }

    @GetMapping("/checkCntCourse2")
    public ResponseEntity<Integer> checkCntCourse2(@RequestParam("id") Integer progid) {
        int updatedCount = continuingCourseCheckerService.updateFailedOrPassedCritiaStudent(progid);
        return ResponseEntity.ok(updatedCount);
    }
}
