package com.ousl.graduation_completion.repository;
import com.ousl.graduation_completion.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findById(Long Long );
}
