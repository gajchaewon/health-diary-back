package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.comment.CommentUpdateDto;
import com.bodytok.healthdiary.dto.comment.CommentWithDiaryResponse;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.repository.CommentRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bodytok.healthdiary.exepction.CustomError.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final UserAccountRepository userAccountRepository;
    private final CommentRepository commentRepository;
    private final PersonalExerciseDiaryRepository diaryRepository;


    @Transactional(readOnly = true)
    public List<CommentDto> searchDiaryComments(Long diaryId) {
        return commentRepository.findByPersonalExerciseDiary_Id(diaryId)
                .stream().map(CommentDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CommentWithDiaryResponse> getAllCommentsByUserId(Long userId) {
        return commentRepository.findByUserAccount_Id(userId)
                .orElseThrow(() -> new CustomBaseException(COMMENT_NOT_FOUND))
                .stream().map(CommentWithDiaryResponse::from)
                .collect(Collectors.toList());
    }


    public CommentDto saveDiaryComment(CommentDto dto) {
        PersonalExerciseDiary PersonalExerciseDiary = diaryRepository.getReferenceById(dto.personalExerciseDiaryId());
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());

        Comment savedComment = commentRepository.save(
                dto.toEntity(PersonalExerciseDiary, userAccount)
        );
        return CommentDto.from(savedComment);
    }

    // 댓글 수정
    public CommentDto updateDiaryComment(Long commentId, CommentUpdateDto updateDto, CustomUserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomBaseException(COMMENT_NOT_FOUND));

        if (!userDetails.getId().equals(comment.getUserAccount().getId())) {
            throw new CustomBaseException(COMMENT_NOT_OWNER);
        }

        String content = updateDto.content();
        // TODO : dto refactoring 할 때 validation 라이브러리로 검증하기
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
        }
        //댓글 수정 및 저장
        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        return CommentDto.from(updatedComment);
    }

    public void deleteDiaryComment(Long commentId, Long userId) {
        commentRepository.deleteByIdAndUserAccount_Id(commentId, userId);
    }
}
