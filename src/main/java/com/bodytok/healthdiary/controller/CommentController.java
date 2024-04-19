package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.*;
import com.bodytok.healthdiary.dto.diary.response.DiaryWithCommentResponse;
import com.bodytok.healthdiary.exepction.CommonApiError;
import com.bodytok.healthdiary.service.CommentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment")
public class CommentController {


    private final CommentService commentService;

    @GetMapping("/all/{userId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentWithDiaryResponse.class))
    })
    @ApiResponse(responseCode = "401, 404", description = "UNAUTHORIZED, NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<List<CommentWithDiaryResponse>> getAllComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "userId") Long userId
    ) {
        //내 댓글만 볼 수 있게 검증
        if (!Objects.equals(userDetails.getId(), userId)){
            throw new AccessDeniedException("댓글 주인만 볼 수 있습니다.");
        }
        List<CommentWithDiaryResponse> comments = commentService.getAllCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }


    @PostMapping("/new")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
    })
    @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<CommentResponse> postNewComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentRequest commentRequest
    ) {
        CommentDto commentDto = commentService.saveDiaryComment(commentRequest.toDto(userDetails.toDto()));

        return ResponseEntity.ok().body(CommentResponse.from(commentDto));
    }

    @PutMapping("/{commentId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
    })
    @ApiResponse(responseCode = "400,401,404", description = "BAD REQUEST, UNAUTHORIZED, NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentUpdateDto updateDto
            ){
        CommentDto commentDto = commentService.updateDiaryComment(commentId, updateDto, userDetails);

        return ResponseEntity.ok().body(CommentResponse.from(commentDto));
    }

    @DeleteMapping("/{commentId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "Successfully deleted"))
    })
    @ApiResponse(responseCode = "404", description = "NOT FOUND", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommonApiError.class))
    })
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId
    ) {
        commentService.deleteDiaryComment(commentId, userDetails.toDto().id());

        return ResponseEntity.ok().body("Successfully deleted");
    }

}
