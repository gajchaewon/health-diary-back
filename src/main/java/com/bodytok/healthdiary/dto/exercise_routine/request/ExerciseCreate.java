package com.bodytok.healthdiary.dto.exercise_routine.request;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ExerciseCreate(
        @NotBlank(message = "운동 이름을 바르게 작성해 주세요.")
        String exerciseName,
        String description
) {

    public static ExerciseCreate of(String exerciseName, String description){
        return new ExerciseCreate(exerciseName, description);
    }

    public Exercise toEntity(){
        return Exercise.of(
                exerciseName,
                description
        );
    }

    public RoutineDto toRoutineDto(Long routineId) {
        return RoutineDto.of(
                routineId,
                null,
                null,
                null,
                List.of(toEntity())
        );
    }
}
