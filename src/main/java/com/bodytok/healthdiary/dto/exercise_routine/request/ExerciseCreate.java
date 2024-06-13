package com.bodytok.healthdiary.dto.exercise_routine.request;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;

import java.util.List;

public record ExerciseCreate(
        Long routineId,
        String exerciseName,
        String description
) {

    public static ExerciseCreate of(Long routineId, String exerciseName, String description){
        return new ExerciseCreate(routineId, exerciseName, description);
    }

    public Exercise toEntity(){
        return Exercise.of(
                exerciseName,
                description
        );
    }

    public RoutineDto toRoutineDto() {
        return RoutineDto.of(
                routineId,
                null,
                null,
                null,
                List.of(toEntity())
        );
    }
}
