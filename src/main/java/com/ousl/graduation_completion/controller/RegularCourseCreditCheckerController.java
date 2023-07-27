package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/regular-course-credits")
public class RegularCourseCreditCheckerController {

    @Autowired
    RegularCourseCreditCheckerService regularCourseCreditCheckerService;

    @GetMapping(value = "/check-courses-need-to-be-converted")
    public ResponseEntity<?>checkCoursesNeedToBeConverted(@RequestParam(value = "programId") Long programId){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkCoursesNeedToBeConverted(programId));
    }

    @GetMapping(value = "/check-s1-regular-course-credits-passed")
    public ResponseEntity<?>checkS1RegularCourseCredits(@RequestParam Long programId,@RequestParam Integer level){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkS1RegularCourseCredits(programId,level));
        //return ResponseEntity.ok().body("test");
    }

}
