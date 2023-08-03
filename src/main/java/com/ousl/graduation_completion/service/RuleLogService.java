package com.ousl.graduation_completion.service;

import com.ousl.graduation_completion.models.RuleLog;

import java.util.List;

public interface RuleLogService {

    List<RuleLog> findRuleLogs(Long student);
}
