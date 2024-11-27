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
import org.ohrim.taskmanagementsystem.dto.task.PaginatedTasksResponse;
import org.ohrim.taskmanagementsystem.dto.task.TaskRequest;
import org.ohrim.taskmanagementsystem.dto.task.TaskResponse;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.mapper.TaskMapper;
import org.ohrim.taskmanagementsystem.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "APIs for managing tasks including creation, update, assignment, and status changes")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest, Authentication authentication) {
        String authorEmail = authentication.getName();
        Task task = taskService.createTask(taskRequest, authorEmail);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{taskId}")
    @Operation(
            summary = "Update a task",
            description = "Updates the details of an existing task by taskId."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )

    })
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequest taskRequest) {
        Task task = taskService.updateTask(taskId, taskRequest);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{taskId}/executor")
    @Operation(
            summary = "Assign an executor to a task",
            description = "Assigns an executor to a task using the taskId and executor's email."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Executor successfully assigned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task or executor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TaskResponse> assignExecutor(@PathVariable Long taskId, @RequestParam String executorEmail) {
        Task task = taskService.assignExecutor(taskId, executorEmail);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{taskId}/status")
    @Operation(
            summary = "Change task status",
            description = "Changes the status of a task. The status is provided as a request parameter."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task status successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid status value",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to change the status of the task",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TaskResponse> changeStatus(
            @PathVariable Long taskId,
            @RequestParam Status status,
            Authentication auth) {

        String userEmail = auth.getName();
        boolean isAdmin = taskService.isAdmin(auth);


        if (!isAdmin && !taskService.canAccessTask(taskId, userEmail)) {
            throw new AccessDeniedException("You do not have permission to change the status of this task.");
        }

        Task task = taskService.changeStatus(taskId, status, userEmail);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its taskId."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to change the status of the task",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/author")
    @Operation(
            summary = "Get tasks by author",
            description = "Retrieves a paginated list of tasks created by a specific author."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved tasks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedTasksResponse.class))
            )
    })
    public ResponseEntity<PaginatedTasksResponse> getTasksByAuthor(
            @RequestParam String authorEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Task> tasksPage = taskService.getTasksByAuthor(authorEmail, page, size);
        PaginatedTasksResponse taskListResponse = taskMapper.toPaginatedTasksResponse(tasksPage);
        return ResponseEntity.ok(taskListResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/executor")
    @Operation(
            summary = "Get tasks by executor",
            description = "Retrieves a paginated list of tasks assigned to a specific executor."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved tasks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedTasksResponse.class))
            ),
    })
    public ResponseEntity<PaginatedTasksResponse> getTasksByExecutor(
            @RequestParam String executorEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Task> tasksPage = taskService.getTasksByExecutor(executorEmail, page, size);
        PaginatedTasksResponse taskListResponse = taskMapper.toPaginatedTasksResponse(tasksPage);
        return ResponseEntity.ok(taskListResponse);
    }

}
