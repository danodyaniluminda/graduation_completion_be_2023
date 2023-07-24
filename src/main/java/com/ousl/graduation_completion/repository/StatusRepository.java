package com.ousl.graduation_completion.repository;

import com.ousl.graduation_completion.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}