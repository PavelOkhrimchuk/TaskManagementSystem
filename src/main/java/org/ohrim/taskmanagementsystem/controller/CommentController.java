package org.ohrim.taskmanagementsystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.comment.CommentPageResponse;
import org.ohrim.taskmanagementsystem.dto.comment.CommentRequest;
import org.ohrim.taskmanagementsystem.dto.comment.CommentResponse;
import org.ohrim.taskmanagementsystem.entity.Comment;
import org.ohrim.taskmanagementsystem.mapper.CommentMapper;
import org.ohrim.taskmanagementsystem.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{taskId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication auth) {
        String authorEmail = auth.getName();
        Comment comment = commentService.addComment(taskId, commentRequest.getContent(), authorEmail);

        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        return ResponseEntity.ok(commentResponse);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{taskId}")
    public ResponseEntity<CommentPageResponse> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Comment> comments = commentService.getCommentsByTask(taskId, page, size);

        CommentPageResponse commentListResponse = commentMapper.toCommentPageResponse(comments);
        return ResponseEntity.ok(commentListResponse);
    }




}
