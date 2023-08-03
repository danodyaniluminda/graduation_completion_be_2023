package com.ousl.graduation_completion.controller;


import com.ousl.graduation_completion.models.RuleLog;
import com.ousl.graduation_completion.service.RuleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/graduation-completion/rule_logs")
public class RuleLogController {

    @Autowired
    RuleLogService ruleLogService;

    @GetMapping(value ="/rule_logs_find_by_application_id")
    public List<RuleLog> getAllRuleLogs(@RequestParam("id") Long student){
    return ruleLogService.findRuleLogs(student);
    }

}
