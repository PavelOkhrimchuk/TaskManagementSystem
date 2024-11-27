package org.ohrim.taskmanagementsystem.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.ErrorResponse;
import org.ohrim.taskmanagementsystem.dto.comment.CommentPageResponse;
import org.ohrim.taskmanagementsystem.dto.comment.CommentRequest;
import org.ohrim.taskmanagementsystem.dto.comment.CommentResponse;
import org.ohrim.taskmanagementsystem.entity.Comment;
import org.ohrim.taskmanagementsystem.mapper.CommentMapper;
import org.ohrim.taskmanagementsystem.service.CommentService;
import org.ohrim.taskmanagementsystem.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "Comments", description = "APIs for managing comments related to tasks")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final TaskService taskService;



    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{taskId}")
    @Operation(
            summary = "Add a comment to a task",
            description = "Adds a comment to a task specified by its ID. Only available to users with USER or ADMIN roles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User non executor does not have permission to comment or access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication auth) {

        String userEmail = auth.getName();
        boolean isAdmin = taskService.isAdmin(auth);
        if (!isAdmin && !taskService.canAccessTask(taskId, userEmail)) {
            throw new AccessDeniedException("You do not have permission to change the status of this task.");
        }

        Comment comment = commentService.addComment(taskId, commentRequest.getContent(), auth.getName());
        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        return ResponseEntity.ok(commentResponse);


    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{taskId}")
    @Operation(
            summary = "Get comments for a task",
            description = "Retrieves a paginated list of comments for a task specified by its ID. Accessible by users with USER or ADMIN roles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentPageResponse.class))
            )
    })
    public ResponseEntity<CommentPageResponse> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Comment> comments = commentService.getCommentsByTask(taskId, page, size);

        CommentPageResponse commentListResponse = commentMapper.toCommentPageResponse(comments);
        return ResponseEntity.ok(commentListResponse);
    }




}
