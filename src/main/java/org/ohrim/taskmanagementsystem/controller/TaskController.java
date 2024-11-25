package org.ohrim.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.task.PaginatedTasksResponse;
import org.ohrim.taskmanagementsystem.dto.task.TaskRequest;
import org.ohrim.taskmanagementsystem.dto.task.TaskResponse;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.mapper.TaskMapper;
import org.ohrim.taskmanagementsystem.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest, Authentication authentication) {

        String authorEmail = authentication.getName();
        Task task = taskService.createTask(taskRequest, authorEmail);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequest taskRequest) {

        Task task = taskService.updateTask(taskId, taskRequest);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{taskId}/executor")
    public ResponseEntity<TaskResponse> assignExecutor(@PathVariable Long taskId, @RequestParam String executorEmail) {
        Task task = taskService.assignExecutor(taskId, executorEmail);
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable Long taskId, @RequestParam Status status,
                                                     Authentication authentication) {
        Task task = taskService.changeStatus(taskId, status, authentication.getName());
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/author")
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
    public ResponseEntity<PaginatedTasksResponse> getTasksByExecutor(
            @RequestParam String executorEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Task> tasksPage = taskService.getTasksByExecutor(executorEmail, page, size);
        PaginatedTasksResponse taskListResponse = taskMapper.toPaginatedTasksResponse(tasksPage);
        return ResponseEntity.ok(taskListResponse);
    }


}
