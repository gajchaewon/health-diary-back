package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.request.ExerciseCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineUpdate;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bodytok.healthdiary.exepction.CustomError.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ExerciseRoutineService {

    private final RoutineRepository routineRepository;
    private final UserAccountService userAccountService;

    @Transactional(readOnly = true)
    public List<RoutineDto> getAllRoutine(Long requestUserId) {
        return routineRepository.findAllByUserAccount_Id(requestUserId)
                .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND))
                .stream().map(RoutineDto::from)
                .collect(Collectors.toList());
    }

    public RoutineDto saveRoutine(RoutineCreate routineCreate, Long userId) {
            UserAccount userAccount = userAccountService.getUserById(userId);
            Routine newRoutine = routineCreate.toEntity(userAccount);
            return RoutineDto.from(routineRepository.save(newRoutine));
    }

    public RoutineDto saveExercise(ExerciseCreate exerciseCreate) {
            Routine routine = routineRepository.findById(exerciseCreate.routineId())
                    .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));

            Exercise exercise = exerciseCreate.toEntity();

            //운동 추가
            routine.getExercises().add(exercise);

            return RoutineDto.from(routineRepository.save(routine));
    }

    public RoutineDto updateRoutine(Long routineId, RoutineUpdate routineUpdate) {
            Routine routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));
            // TODO : dto 변환 후 업데이트
            routine.updateRoutineInfo(routineUpdate);
            return RoutineDto.from(routineRepository.save(routine));
    }

    public Long deleteRoutine(Long routineId, Long userId) {
            Routine routine = routineRepository.findById(routineId).orElseThrow(
                    () -> new CustomBaseException(ROUTINE_NOT_FOUND));
            if (!routine.getUserAccount().getId().equals(userId)) {
                throw new CustomBaseException(ROUTINE_NOT_OWNER);
            }
            routineRepository.delete(routine);
            return routineId;
    }

    public RoutineDto deleteExercise(Long routineId, String exerciseId, Long userId) {
            Routine routine = routineRepository.findById(routineId).orElseThrow(
                    () -> new CustomBaseException(ROUTINE_NOT_FOUND));

            //id 같은 운동 삭제하기
            routine.removeExercise(exerciseId);
            return RoutineDto.from(routineRepository.save(routine));
    }
}
