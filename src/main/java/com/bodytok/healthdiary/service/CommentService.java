package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.repository.CommentRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public CommentDto updateDiaryComment(CommentDto dto) {
        try {
            Comment comment = commentRepository.getReferenceById(dto.id());
            if (dto.content() != null) { //입력된 내용이 있으면 수정해서 저장
                comment.setContent(dto.content());

                return CommentDto.from(comment);
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
            return null;
        }
        return null;
    }

    public void deleteDiaryComment(Long commentId, Long userId) {
            commentRepository.deleteByIdAndUserAccount_Id(commentId, userId);
    }
}
