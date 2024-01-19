package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.ExerciseTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ExerciseTimeLogRepository extends JpaRepository<ExerciseTimeLog, Long> {
}