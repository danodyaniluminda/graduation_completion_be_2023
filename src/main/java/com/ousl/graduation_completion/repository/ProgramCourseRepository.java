package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.ProgramCourse;
import com.ousl.graduation_completion.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {
}
