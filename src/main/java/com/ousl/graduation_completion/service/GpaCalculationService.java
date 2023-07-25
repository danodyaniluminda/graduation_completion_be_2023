package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.dto.DataObject;

import java.util.HashMap;

public interface GpaCalculationService {

    DataObject checkConsideredApplications(Long programId);


    HashMap<String, Object> gpaCalculations(Long programId);

}
