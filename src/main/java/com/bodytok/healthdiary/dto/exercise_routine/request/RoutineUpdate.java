package com.bodytok.healthdiary.dto.exercise_routine.request;


import jakarta.validation.constraints.NotBlank;

public record RoutineUpdate(

        @NotBlank(message = "루틴 이름을 바르게 작성해 주세요.")
        String routineName,
        String memo
) {
}
