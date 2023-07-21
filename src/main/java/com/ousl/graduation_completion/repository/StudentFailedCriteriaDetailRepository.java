package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Criterion;
import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.models.StudentFailedCriteriaDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFailedCriteriaDetailRepository extends JpaRepository<StudentFailedCriteriaDetail, Long> {

    void deleteAllByCriteriaAndProgram(Criterion criterion, Program program);

    List<StudentFailedCriteriaDetail> getAllByCriteriaAndStatusAndProgram(Criterion criteria, String status, Program programId);
}