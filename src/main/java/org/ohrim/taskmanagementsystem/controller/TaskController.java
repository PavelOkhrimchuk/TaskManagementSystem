package org.ohrim.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.TaskRequest;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest, Authentication authentication) {
        String authorEmail = authentication.getName();
        Task task = taskService.createTask(
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getPriority(),
                authorEmail,
                taskRequest.getExecutorEmail()
        );
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest taskRequest) {
        Task task = taskService.updateTask(taskId, taskRequest.getTitle(),
                taskRequest.getDescription(), taskRequest.getPriority(),
                taskRequest.getExecutorEmail());
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{taskId}/executor")
    public ResponseEntity<Task> assignExecutor(@PathVariable Long taskId, @RequestParam String executorEmail) {
        Task task = taskService.assignExecutor(taskId, executorEmail);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> changeStatus(@PathVariable Long taskId, @RequestParam Status status,
                                             Authentication authentication) {
        Task task = taskService.changeStatus(taskId, status, authentication.getName());
        return ResponseEntity.ok(task);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }



}
