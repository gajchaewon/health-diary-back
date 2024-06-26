package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Comment;
import com.bodytok.healthdiary.domain.PersonalExerciseDiary;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.comment.response.MyCommentsResponse;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.comment.CommentRepository;
import com.bodytok.healthdiary.repository.PersonalExerciseDiaryRepository;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bodytok.healthdiary.exepction.CustomError.COMMENT_NOT_FOUND;
import static com.bodytok.healthdiary.exepction.CustomError.COMMENT_NOT_OWNER;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final UserAccountRepository userAccountRepository;
    private final CommentRepository commentRepository;
    private final PersonalExerciseDiaryRepository diaryRepository;


    @Transactional(readOnly = true)
    public MyCommentsResponse getAllCommentsByUserId(Long userId) {
        return commentRepository.findCommentsByUserId(userId);
    }


    public CommentDto saveDiaryComment(CommentDto dto) {
        PersonalExerciseDiary PersonalExerciseDiary = diaryRepository.getReferenceById(dto.diaryDto().id());
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().id());

        Comment savedComment = commentRepository.save(
                dto.toEntity(PersonalExerciseDiary, userAccount)
        );
        return CommentDto.from(savedComment);
    }

    // 댓글 수정
    public CommentDto updateDiaryComment(CommentDto dto) {
        Comment comment = commentRepository.findById(dto.id())
                .orElseThrow(() -> new CustomBaseException(COMMENT_NOT_FOUND));
        Long userId = dto.userAccountDto().id();
        if (! userId.equals(comment.getUserAccount().getId())) {
            throw new CustomBaseException(COMMENT_NOT_OWNER);
        }
        //댓글 수정 및 저장
        comment.setContent(dto.content());
        Comment updatedComment = commentRepository.save(comment);
        return CommentDto.from(updatedComment);
    }

    public void deleteDiaryComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new CustomBaseException(COMMENT_NOT_FOUND));
        if (! userId.equals(comment.getUserAccount().getId())) {
            throw new CustomBaseException(COMMENT_NOT_OWNER);
        }
        commentRepository.deleteByIdAndUserAccount_Id(commentId, userId);
    }
}
