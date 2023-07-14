package com.ousl.graduation_completion.repository;
import com.ousl.graduation_completion.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findApplicationByNameAndId(Optional<Application> name, Long l);
}
