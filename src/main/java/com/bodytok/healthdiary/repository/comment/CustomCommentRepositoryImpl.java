package com.bodytok.healthdiary.repository.comment;

import com.bodytok.healthdiary.domain.QComment;
import com.bodytok.healthdiary.domain.QUserAccount;
import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.comment.response.CommentWithDiaryResponse;
import com.bodytok.healthdiary.dto.comment.response.MyCommentsResponse;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public MyCommentsResponse findCommentsByUserId(Long userId) {
        QComment comment = QComment.comment;
        QUserAccount userAccount = QUserAccount.userAccount;

        List<CommentWithDiaryResponse> comments = queryFactory.selectFrom(comment)
                .where(comment.userAccount.id.eq(userId))
                .orderBy(comment.createdAt.desc())
                .fetch()
                .stream()
                .map(CommentDto::from)
                .map(CommentWithDiaryResponse::from)
                .toList();

        UserAccount user = queryFactory.selectFrom(userAccount)
                .where(userAccount.id.eq(userId))
                .fetchOne();
        if (user == null) {
            throw new CustomBaseException(CustomError.USER_NOT_FOUND);
        }
        return MyCommentsResponse.builder()
                .userInfo(UserResponse.from(
                        UserAccountDto.from(user)
                ))
                .comments(comments)
                .build();
    }

}
