package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/graduation-completion/regular-course-credits")
public class RegularCourseCreditCheckerController {

    @Autowired
    RegularCourseCreditCheckerService regularCourseCreditCheckerService;

    @GetMapping(value = "/check-courses-need-to-be-converted")
    public ResponseEntity<?>checkCoursesNeedToBeConverted(@RequestParam(value = "programId") Long programId){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkCoursesNeedToBeConverted(programId));
    }

    @GetMapping(value = "/check-regular-course-credits-passed")
    public ResponseEntity<?>checkRegularCourseCredits(@RequestParam Long programId,@RequestParam Integer level, @RequestParam Integer noOfCreditsRequired){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkRegularCourseCredits(programId,level,noOfCreditsRequired));
        //return ResponseEntity.ok().body("test");
    }

}
