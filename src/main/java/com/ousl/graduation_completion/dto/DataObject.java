package com.ousl.graduation_completion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataObject {
    private String status;
    private String message;
    private Object data;
}
