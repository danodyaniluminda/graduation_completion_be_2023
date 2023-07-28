package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.RuleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleLogRepository extends JpaRepository<RuleLog, Long> {}