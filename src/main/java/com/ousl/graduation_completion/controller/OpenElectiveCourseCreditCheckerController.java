package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.service.OpenElectiveCourseCreditCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/optional-course-credits")
public class OpenElectiveCourseCreditCheckerController {

    @Autowired
    OpenElectiveCourseCreditCheckerService openElectiveCourseCreditCheckerService;
    @GetMapping(value = "/check-courses-need-to-be-converted")
    public ResponseEntity<?> checkCoursesNeedToBeConverted(@RequestParam("id") Integer programId,@RequestParam("level") Integer level){
        return ResponseEntity.ok().body(openElectiveCourseCreditCheckerService.checkCoursesNeedToBeConverted(programId, level));
    }

    @GetMapping(value = "/check-open-elective-coursed-level-tree")
    public ResponseEntity<?> checkOpenElectiveCourseLevelTree(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(openElectiveCourseCreditCheckerService.checkOpenElectiveCourseLevelThree(programId));
    }

    @GetMapping(value = "/check-open-elective-coursed-level-five")
    public ResponseEntity<?> checkOpenElectiveCourseLevelFive(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(openElectiveCourseCreditCheckerService.checkOpenElectiveCourseLevelFive(programId));
    }



}
