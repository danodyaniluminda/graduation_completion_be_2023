package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.RuleLog;
import com.ousl.graduation_completion.models.StudentFailedCriteriaDetail;
import com.ousl.graduation_completion.repository.RuleLogRepository;
import com.ousl.graduation_completion.service.RuleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleLogImpl implements RuleLogService {

    @Autowired
    RuleLogRepository ruleLogRepository;

    @Override
    public List<RuleLog> findRuleLogs(Long student) {
        return ruleLogRepository.findByStudent(student);
    }


}
