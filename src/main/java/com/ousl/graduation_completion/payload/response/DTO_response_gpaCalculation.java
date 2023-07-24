package com.ousl.graduation_completion.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTO_response_gpaCalculation {
    private Long program_id;
    private Long student_id;
    private Double gpa;

}
