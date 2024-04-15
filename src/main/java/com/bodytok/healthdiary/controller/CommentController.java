package com.bodytok.healthdiary.controller;


import com.bodytok.healthdiary.domain.security.CustomUserDetails;
import com.bodytok.healthdiary.dto.comment.CommentDto;
import com.bodytok.healthdiary.dto.comment.CommentRequest;
import com.bodytok.healthdiary.dto.comment.CommentResponse;
import com.bodytok.healthdiary.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment")
public class CommentController {


    private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<CommentResponse> postNewComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CommentRequest commentRequest
    ) {
        CommentDto commentDto = commentService.saveDiaryComment(commentRequest.toDto(userDetails.toDto()));

        return ResponseEntity.ok().body(CommentResponse.from(commentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "commentId") Long commentId
    ) {
        commentService.deleteDiaryComment(commentId, userDetails.toDto().id());

        return ResponseEntity.ok().body("Successfully deleted");
    }

}
