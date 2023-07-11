package com.ousl.graduation_completion.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/sample")
public class sampleController {
//    @GetMapping(value = "/get_list")
//    public List<?> getAllCountries() throws JsonProcessingException {
//        List list = new ArrayList();
//        list.add('fds');
//        return List.of();
//    }
    @GetMapping(value = "/get_list")
    public String getName(){
        return "Ja";
    }
}
