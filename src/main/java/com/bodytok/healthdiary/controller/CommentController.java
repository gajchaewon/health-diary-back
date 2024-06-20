package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.*;
import com.bodytok.healthdiary.dto.comment.request.CommentCreate;
import com.bodytok.healthdiary.exepction.ApiErrorResponse;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.exepction.CustomError;
import com.bodytok.healthdiary.service.CommentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment")
public class CommentController {
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;
    private final CommentService commentService;

    @GetMapping("/all/{userId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentWithDiaryResponse.class))
    })
    @ApiResponse(responseCode = "401, 404", description = "UNAUTHORIZED, NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    })
    public ResponseEntity<List<CommentWithDiaryResponse>> getAllComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "userId") Long userId
    ) {
        //내 댓글만 볼 수 있게 검증
        if (!userDetails.getId().equals(userId)) {
            throw new CustomBaseException(CustomError.COMMENT_NOT_OWNER);
        }
        List<CommentDto> commentDtoList = commentService.getAllCommentsByUserId(userId);
        List<CommentWithDiaryResponse> response = commentDtoList.stream()
                .map(commentMapper::toCommentWithDiaryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/new")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
    })
    @ApiResponse(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    })
    public ResponseEntity<CommentResponse> postNewComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentCreate commentCreate
    ) {
        var toDto = commentMapper.toDtoFromCreate(commentCreate, userDetails.toDto());
        CommentDto commentDto = commentService.saveDiaryComment(toDto);
        return ResponseEntity.ok().body(commentMapper.toResponse(commentDto));
    }

    @PreAuthorize("hasPermission(#commentId, 'COMMENT', 'UPDATE')")
    @PutMapping("/{commentId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
    })
    @ApiResponse(responseCode = "400,401,404", description = "BAD REQUEST, UNAUTHORIZED, NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    })
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentUpdate commentUpdate
    ) {

        var toDto = commentMapper.toDtoFromUpdate(commentId, commentUpdate, userDetails.toDto());
        CommentDto commentDto = commentService.updateDiaryComment(toDto);

        return ResponseEntity.ok().body(commentMapper.toResponse(commentDto));
    }

    @PreAuthorize("hasPermission(#commentId, 'COMMENT', 'DELETE')")
    @DeleteMapping("/{commentId}")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
    })
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId
    ) {
        commentService.deleteDiaryComment(commentId, userDetails.getId());

        return ResponseEntity.noContent().build();
    }

}
