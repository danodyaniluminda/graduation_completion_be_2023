package com.ousl.graduation_completion.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTO_response_gpaCalculation {
    private Integer programme_id;
    private Integer application_id;
    private Double gpa;


}
