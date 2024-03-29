package com.ousl.graduation_completion.controller;
import com.ousl.graduation_completion.service.GradeCalculationLevelThreeToFiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/grade_calculation")
public class GradeCalculationLevelThreeToFiveController {
    @Autowired
    GradeCalculationLevelThreeToFiveService gradeCalculationLevelThreeToFiveService;
    @GetMapping(value = "/level_three")
    public ResponseEntity<?> validateLevelThreeGrade(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(gradeCalculationLevelThreeToFiveService.gradeCalculationLevelThree(programId));
    }

    @GetMapping(value = "/level_four")
    public ResponseEntity<?> validateLevelFourGrade(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(gradeCalculationLevelThreeToFiveService.gradeCalculationLevelFour(programId));
    }

    @GetMapping(value = "/level_five")
    public ResponseEntity<?> validateLevelFiveGrade(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(gradeCalculationLevelThreeToFiveService.gradeCalculationLevelFive(programId));
    }
}