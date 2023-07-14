package com.ousl.graduation_completion.controller;

import com.ousl.graduation_completion.payload.request.DTO_request_gpaCalculation;
import com.ousl.graduation_completion.payload.response.DTO_response_gpaCalculation;
import com.ousl.graduation_completion.service.GpaCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/graduation-completion/gpa_calculation")
public class GpaCalculationController {

    @Autowired
    GpaCalculationService gpaCalculationService;

    @PostMapping("/calculate_gpa")
    public List<DTO_response_gpaCalculation> calGpa(@RequestBody DTO_request_gpaCalculation dtoRequestGpaCalculation){
        return gpaCalculationService.gpaCalculations(dtoRequestGpaCalculation);
    }

}
