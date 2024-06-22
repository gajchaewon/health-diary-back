package com.bodytok.healthdiary.dto.exercise_routine.request;


import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;
import jakarta.validation.constraints.NotBlank;

public record RoutineCreate(

        @NotBlank(message = "루틴 이름을 바르게 작성해 주세요.")
        String routineName,
        String memo
) {

    public static RoutineCreate of(String routineName, String memo) {
        return new RoutineCreate(routineName,memo);
    }

    public Routine toEntity(UserAccount userAccount){
        return Routine.of(
                routineName,
                memo,
                userAccount
        );
    }
}
