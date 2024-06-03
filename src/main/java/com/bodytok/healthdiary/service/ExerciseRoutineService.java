package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.ExerciseRoutine;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.exercise_routine.ExerciseDto;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.request.ExerciseCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.repository.ExerciseRepository;
import com.bodytok.healthdiary.repository.ExerciseRoutineRepository;
import com.bodytok.healthdiary.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ExerciseRoutineService {

    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final ExerciseRoutineRepository exerciseRoutineRepository;
    private final UserAccountService userAccountService;

    @Transactional(readOnly = true)
    public List<RoutineDto> getAllRoutine(Long requestUserId) {
        List<Routine> routines = routineRepository.findAllByUserAccount_Id(requestUserId);
        return routines.stream().map(RoutineDto::from).collect(Collectors.toList());
    }

    public RoutineDto saveRoutine(RoutineCreate routineCreate, Long userId){
        UserAccount  userAccount = userAccountService.getUserById(userId);
        Routine newRoutine = routineCreate.toEntity(userAccount);
        return RoutineDto.from(routineRepository.save(newRoutine));
    }

    public ExerciseDto saveExercise(ExerciseCreate exerciseCreate){
        Routine routine = routineRepository.findById(exerciseCreate.routineId()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 루틴이 없습니다. id -> " + exerciseCreate.routineId())
        );
        Exercise newExercise = exerciseRepository.save(exerciseCreate.toEntity());

        ExerciseRoutine exerciseRoutine = ExerciseRoutine.of(routine, newExercise);

        exerciseRoutineRepository.save(exerciseRoutine);

        return ExerciseDto.from(newExercise);
    }


}
