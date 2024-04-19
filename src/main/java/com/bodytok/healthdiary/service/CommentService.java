package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.comment.CommentUpdateDto;
import com.bodytok.healthdiary.dto.comment.CommentWithDiaryResponse;
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

        return commentRepository.findByUserAccount_Id(userId).stream()
                .map(CommentWithDiaryResponse::from)
                .collect(Collectors.toList());
    }


    public CommentDto saveDiaryComment(CommentDto dto) {
        try {
            PersonalExerciseDiary PersonalExerciseDiary = diaryRepository.getReferenceById(dto.personalExerciseDiaryId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());

            Comment savedComment = commentRepository.save(
                    dto.toEntity(PersonalExerciseDiary, userAccount)
            );
            return CommentDto.from(savedComment);

        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글에 필요한 정보를 찾을 수 없습니다 - dto: {}", e.getLocalizedMessage());
            return null;
        }
    }

    // 댓글 수정
    public CommentDto updateDiaryComment(Long commentId, CommentUpdateDto updateDto, CustomUserDetails userDetails){
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다. commentId : " + commentId));

            if (!Objects.equals(userDetails.getId(), comment.getUserAccount().getId())) {
                throw new AccessDeniedException("댓글 작성자만 수정할 수 있습니다.");
            }

            String content = updateDto.content();
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
            }
            //댓글 수정 및 저장
            comment.setContent(content);
            Comment updatedComment = commentRepository.save(comment);
            return CommentDto.from(updatedComment);
        } catch (EntityNotFoundException | AccessDeniedException | IllegalArgumentException e) {
            throw e;
        }
    }

    public void deleteDiaryComment(Long commentId, Long userId) {
        commentRepository.deleteByIdAndUserAccount_Id(commentId, userId);
    }
}
