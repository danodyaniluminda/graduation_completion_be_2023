package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.payload.request.DTO_request_gpaCalculation;
import com.ousl.graduation_completion.payload.response.DTO_response_gpaCalculation;
import com.ousl.graduation_completion.service.GpaCalculationService;
import com.ousl.graduation_completion.service.impl.GpaCalculationserviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/graduation-completion/gpa_calculation")
public class GpaCalculationController {

    @Autowired
    GpaCalculationService gpaCalculationService;

    @Autowired
    GpaCalculationserviceImpl gpaCalculationservice;

    @GetMapping(value = "/check_considered_application")
    public ResponseEntity<?> checkConsideredApplications(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(gpaCalculationService.checkConsideredApplications(programId));
    }

    @GetMapping(value = "/calculate_gpa")
    public ResponseEntity<?> calGpa(@RequestParam("id") Long programId){
        return ResponseEntity.ok().body(gpaCalculationService.gpaCalculations(programId));
    }

}
