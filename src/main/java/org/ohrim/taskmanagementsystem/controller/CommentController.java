package org.ohrim.taskmanagementsystem.controller;


import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.entity.Comment;
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


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{taskId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId,
                                              @RequestParam String content,
                                              Authentication auth) {
        String authorEmail = auth.getName();
        Comment comment = commentService.addComment(taskId, content, authorEmail);
        return ResponseEntity.ok(comment);
    }



    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{taskId}")
    public ResponseEntity<Page<Comment>> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Comment> comments = commentService.getCommentsByTask(taskId, page, size);
        return ResponseEntity.ok(comments);
    }



}
