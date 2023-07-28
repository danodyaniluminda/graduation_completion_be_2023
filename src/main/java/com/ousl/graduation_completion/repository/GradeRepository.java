package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.ALLTables;
import com.ousl.graduation_completion.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade,Long> {
//    List<Grade> findGradeById(Optional<Grade> grade , Long l);
}
