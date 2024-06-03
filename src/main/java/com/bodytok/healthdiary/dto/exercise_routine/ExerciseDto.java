package com.bodytok.healthdiary.dto.exercise_routine;

import com.bodytok.healthdiary.domain.Exercise;

public record ExerciseDto(
        Long id,
        String exerciseName,
        String description
) {

    public static ExerciseDto of(Long id, String exerciseName, String description) {
        return new ExerciseDto(id, exerciseName, description);
    }

    public static ExerciseDto from(Exercise entity) {
        return of(
                entity.getId(),
                entity.getExerciseName(),
                entity.getDescription()
        );
    }
}
