package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentStatusRepository extends JpaRepository<StudentStatus, Long> {
}
