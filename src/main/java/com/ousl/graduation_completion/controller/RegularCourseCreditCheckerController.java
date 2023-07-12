package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graduation-completion/regular-course-credits")
public class RegularCourseCreditCheckerController {

    @Autowired
    RegularCourseCreditCheckerService regularCourseCreditCheckerService;

    @GetMapping(value = "/check-courses-need-to-be-converted")
    public ResponseEntity<?>checkCoursesNeedToBeConverted(@PathVariable Long programId){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkCoursesNeedToBeConverted(programId));
    }

    @GetMapping(value = "/check-regular-course-credits-passed")
    public ResponseEntity<?>checkRegularCourseCredits(@PathVariable Long applicationId, @PathVariable Long programId,@PathVariable Integer level, @PathVariable Integer noOfCreditsRequired){
        return ResponseEntity.ok().body(regularCourseCreditCheckerService.checkRegularCourseCredits(applicationId,programId,level,noOfCreditsRequired));
    }

}
