package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.payload.request.DTO_request_gpaCalculation;
import com.ousl.graduation_completion.payload.response.DTO_response_gpaCalculation;

import java.util.List;

public interface GpaCalculationService {

    List<DTO_response_gpaCalculation> gpaCalculations(DTO_request_gpaCalculation dtoRequestGpaCalculation);



}
