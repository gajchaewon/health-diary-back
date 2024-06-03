package com.bodytok.healthdiary.dto.exercise_routine.request;


import com.bodytok.healthdiary.domain.Exercise;

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

}
