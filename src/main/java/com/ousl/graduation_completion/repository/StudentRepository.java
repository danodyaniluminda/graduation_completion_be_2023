package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
