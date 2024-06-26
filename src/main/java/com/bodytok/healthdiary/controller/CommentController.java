package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.*;
import com.bodytok.healthdiary.dto.comment.request.CommentCreate;
import com.bodytok.healthdiary.dto.comment.request.CommentUpdate;
import com.bodytok.healthdiary.dto.comment.response.CommentResponse;
import com.bodytok.healthdiary.dto.comment.response.MyCommentsResponse;
import com.bodytok.healthdiary.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment")
public class CommentController {
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;
    private final CommentService commentService;


    @GetMapping("/all")
    @Operation(summary = "모든 댓글 조회")
    public ResponseEntity<MyCommentsResponse> getAllComments(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var response = commentService.getAllCommentsByUserId(userDetails.getId());
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/new")
    @Operation(summary = "새 댓글 작성")
    public ResponseEntity<CommentResponse> postNewComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid CommentCreate commentCreate
    ) {
        var toDto = commentMapper.toDtoFromCreate(commentCreate, userDetails.toDto());
        CommentDto commentDto = commentService.saveDiaryComment(toDto);
        return ResponseEntity.ok().body(commentMapper.toResponse(commentDto));
    }


    @PreAuthorize("hasPermission(#commentId, 'COMMENT', 'UPDATE')")
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 업데이트")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody @Valid CommentUpdate commentUpdate
    ) {

        var toDto = commentMapper.toDtoFromUpdate(commentId, commentUpdate, userDetails.toDto());
        CommentDto commentDto = commentService.updateDiaryComment(toDto);

        return ResponseEntity.ok().body(commentMapper.toResponse(commentDto));
    }


    @PreAuthorize("hasPermission(#commentId, 'COMMENT', 'DELETE')")
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId
    ) {
        commentService.deleteDiaryComment(commentId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }

}
