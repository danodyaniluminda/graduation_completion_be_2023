package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.service.OptionalCourseCreditCheckerService;
import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/optional-course-credits")
public class OptionalCourseCreditCheckerController {

    @Autowired
    OptionalCourseCreditCheckerService optionalCourseCreditCheckerService;
    @GetMapping(value = "/check-courses-need-to-be-converted")
    public ResponseEntity<?> checkCoursesNeedToBeConverted(@RequestParam("id") Integer programId){
        return ResponseEntity.ok().body(optionalCourseCreditCheckerService.checkCoursesNeedToBeConverted(programId));
    }
}
