package org.ohrim.taskmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.task.TaskRequest;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.exception.task.InvalidRequestException;
import org.ohrim.taskmanagementsystem.exception.task.ResourceNotFoundException;
import org.ohrim.taskmanagementsystem.exception.task.TaskStatusException;
import org.ohrim.taskmanagementsystem.repository.TaskRepository;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Task createTask(TaskRequest taskRequest, String authorEmail) {
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        User executor = null;
        if (taskRequest.getExecutorEmail() != null && !taskRequest.getExecutorEmail().isEmpty()) {
            executor = userRepository.findByEmail(taskRequest.getExecutorEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Executor not found"));
        }

        if (taskRequest.getTitle() == null || taskRequest.getTitle().isEmpty()) {
            throw new InvalidRequestException("Task title cannot be null or empty");
        }

        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(Status.IN_PROGRESS)
                .priority(taskRequest.getPriority())
                .author(author)
                .executor(executor)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskRequest.getExecutorEmail() != null) {
            User executor = userRepository.findByEmail(taskRequest.getExecutorEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Executor not found"));
            task.setExecutor(executor);
        }

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setUpdatedAt(Instant.now());

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public Task changeStatus(Long taskId, Status status, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (status == null) {
            throw new TaskStatusException("Invalid task status");
        }

        task.setStatus(status);
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task);
    }



    @Transactional
    public Task assignExecutor(Long taskId, String executorEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User executor = userRepository.findByEmail(executorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Executor not found"));

        task.setExecutor(executor);
        task.setUpdatedAt(Instant.now());

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasksByAuthor(String authorEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByAuthorEmail(authorEmail, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasksByExecutor(String executorEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByExecutorEmail(executorEmail, pageable);
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(Authentication authentication) {

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    @Transactional(readOnly = true)
    public boolean canAccessTask(Long taskId, String userEmail) {

        return taskRepository.existsByIdAndExecutorEmail(taskId, userEmail);
    }








}

