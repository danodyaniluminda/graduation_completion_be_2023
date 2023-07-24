package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
}