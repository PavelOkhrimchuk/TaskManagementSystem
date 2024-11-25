package org.ohrim.taskmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.dto.task.TaskRequest;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.entity.user.Role;
import org.ohrim.taskmanagementsystem.repository.TaskRepository;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        User executor = null;
        if (taskRequest.getExecutorEmail() != null && !taskRequest.getExecutorEmail().isEmpty()) {
            executor = userRepository.findByEmail(taskRequest.getExecutorEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Executor not found"));
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
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (taskRequest.getExecutorEmail() != null) {
            User executor = userRepository.findByEmail(taskRequest.getExecutorEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Executor not found"));
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
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public Task changeStatus(Long taskId, Status status, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));


        if (!task.getExecutor().getEmail().equals(userEmail) && !isAdmin(userEmail)) {
            throw new AccessDeniedException("You are not authorized to change the status of this task");
        }

        task.setStatus(status);
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task);
    }


    private boolean isAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);
    }



    @Transactional
    public Task assignExecutor(Long taskId, String executorEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User executor = userRepository.findByEmail(executorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Executor not found"));

        task.setExecutor(executor);
        task.setUpdatedAt(Instant.now());

        return taskRepository.save(task);
    }


    @Transactional(readOnly = true)
    public Page<Task> getTasksByAuthor(String authorEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByAuthor_Email(authorEmail, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasksByExecutor(String executorEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllByExecutor_Email(executorEmail, pageable);
    }

}

