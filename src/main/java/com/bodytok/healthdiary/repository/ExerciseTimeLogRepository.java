package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.ExerciseTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExerciseTimeLogRepository extends JpaRepository<ExerciseTimeLog, Long> {
}