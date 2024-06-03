package com.bodytok.healthdiary.dto.exercise_routine.response;

import com.bodytok.healthdiary.dto.exercise_routine.ExerciseDto;

public record ExerciseResponse(
        Long id,
        String exerciseName,
        String description
) {

    public static ExerciseResponse of(Long id, String exerciseName, String description){
        return new ExerciseResponse(id, exerciseName, description);
    }

    public static ExerciseResponse from(ExerciseDto dto) {
        return of(
                dto.id(),
                dto.exerciseName(),
                dto.description()
        );
    }
}
