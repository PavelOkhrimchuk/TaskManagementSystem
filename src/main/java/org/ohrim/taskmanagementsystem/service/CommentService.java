package org.ohrim.taskmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.ohrim.taskmanagementsystem.entity.Comment;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.exception.task.ResourceNotFoundException;
import org.ohrim.taskmanagementsystem.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment addComment(Long taskId, String content, String authorEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .task(task)
                .createdAt(Instant.now())
                .build();

        return commentRepository.save(comment);
    }



    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByTask(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findAllByTaskId(taskId, pageable);
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    @Transactional(readOnly = true)
    public boolean canAccessTask(Long taskId, String userEmail, boolean isAdmin) {
        return isAdmin || taskRepository.existsByIdAndExecutorEmail(taskId, userEmail);
    }


}
