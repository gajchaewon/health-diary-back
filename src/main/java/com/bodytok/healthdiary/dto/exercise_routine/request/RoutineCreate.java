package com.bodytok.healthdiary.dto.exercise_routine.request;


import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;

public record RoutineCreate(
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
