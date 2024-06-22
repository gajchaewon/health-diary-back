package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Exercise;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.exercise_routine.RoutineDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bodytok.healthdiary.exepction.CustomError.ROUTINE_NOT_FOUND;

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

        //저장할 운동
        Exercise exercise = dto.exercises().get(0);

        //운동 추가
        routine.getExercises().add(exercise);

        return RoutineDto.from(routineRepository.save(routine));
    }

    public RoutineDto updateRoutine(RoutineDto dto) {

        Routine routine = routineRepository.findById(dto.id())
                .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));

        //정보 업데이트
        routine.updateRoutineInfo(dto);
        return RoutineDto.from(routineRepository.save(routine));
    }

    public void deleteRoutine(RoutineDto dto) {
        Routine routine = routineRepository.findById(dto.id()).orElseThrow(
                () -> new CustomBaseException(ROUTINE_NOT_FOUND));

        routineRepository.delete(routine);
    }

    public RoutineDto deleteExercise(RoutineDto dto, String exerciseId) {
        Routine routine = routineRepository.findById(dto.id()).orElseThrow(
                () -> new CustomBaseException(ROUTINE_NOT_FOUND));

        //id 같은 운동 삭제하기
        routine.removeExercise(exerciseId);
        return RoutineDto.from(routineRepository.save(routine));
    }
}
