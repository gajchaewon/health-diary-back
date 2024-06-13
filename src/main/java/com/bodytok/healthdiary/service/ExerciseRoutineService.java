package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineMapper;
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

    public RoutineDto saveRoutine(RoutineDto dto, Long userId) {
        UserAccount userAccount = userAccountService.getUserById(userId);
        Routine newRoutine = dto.toEntity(userAccount);
        return RoutineDto.from(routineRepository.save(newRoutine));
    }

    public RoutineDto saveExercise(RoutineDto dto) {
        Routine routine = routineRepository.findById(dto.id())
                .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));
        if (!routine.getUserAccount().getId().equals(dto.userAccountDto().id())) {
            throw new CustomBaseException(ROUTINE_NOT_OWNER);
        }
        //저장할 운동
        Exercise exercise = dto.exercises().get(0);

        //운동 추가
        routine.getExercises().add(exercise);

        return RoutineDto.from(routineRepository.save(routine));
    }

    public RoutineDto updateRoutine(RoutineDto dto) {
        Long userId = dto.userAccountDto().id();

        Routine routine = routineRepository.findById(dto.id())
                .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));
        if (!routine.getUserAccount().getId().equals(userId)) {
            throw new CustomBaseException(ROUTINE_NOT_OWNER);
        }
        //정보 업데이트
        routine.updateRoutineInfo(dto);
        return RoutineDto.from(routineRepository.save(routine));
    }

    public void deleteRoutine(RoutineDto dto) {
        Long userId = dto.userAccountDto().id();
        Routine routine = routineRepository.findById(dto.id()).orElseThrow(
                () -> new CustomBaseException(ROUTINE_NOT_FOUND));
        if (!routine.getUserAccount().getId().equals(userId)) {
            throw new CustomBaseException(ROUTINE_NOT_OWNER);
        }
        routineRepository.delete(routine);
    }

    public RoutineDto deleteExercise(RoutineDto dto, String exerciseId) {
        Routine routine = routineRepository.findById(dto.id()).orElseThrow(
                () -> new CustomBaseException(ROUTINE_NOT_FOUND));
        if (!routine.getUserAccount().getId().equals(dto.userAccountDto().id())) {
            throw new CustomBaseException(ROUTINE_NOT_OWNER);
        }
        //id 같은 운동 삭제하기
        routine.removeExercise(exerciseId);
        return RoutineDto.from(routineRepository.save(routine));
    }
}
