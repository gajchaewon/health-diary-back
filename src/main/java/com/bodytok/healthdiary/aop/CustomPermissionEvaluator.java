package com.bodytok.healthdiary.aop;

import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.Routine;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.comment.CommentRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.bodytok.healthdiary.exepction.CustomError.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final RoutineRepository routineRepository;
    private final PersonalExerciseDiaryRepository diaryRepository;
    private final CommentRepository commentRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        TargetType type = TargetType.fromString(targetType);
        log.info(">>>type: {}",type);
        return switch (type) {
            case DIARY -> {
                PersonalExerciseDiary diary = diaryRepository.findById((Long) targetId)
                        .orElseThrow(() -> new CustomBaseException(DIARY_NOT_FOUND));
                yield diary.getUserAccount().getId().equals(userPrincipal.getId());
            }
            case COMMENT -> {
                Comment comment = commentRepository.findById((Long) targetId)
                        .orElseThrow(() -> new CustomBaseException(COMMENT_NOT_FOUND));
                yield comment.getUserAccount().getId().equals(userPrincipal.getId());
            }
            case ROUTINE -> {
                Routine routine = routineRepository.findById((Long) targetId)
                        .orElseThrow(() -> new CustomBaseException(ROUTINE_NOT_FOUND));
                yield  routine.getUserAccount().getId().equals(userPrincipal.getId());
            }
            default -> {
                log.error("[인가 실패] 사용자 소유가 아닙니다.");
                yield false;
            }
        };
    }

    public enum TargetType {
        DIARY,
        COMMENT,
        ROUTINE;

        public static TargetType fromString(String targetType) {
            try {
                return TargetType.valueOf(targetType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid target type: " + targetType);
            }
        }
    }


}

