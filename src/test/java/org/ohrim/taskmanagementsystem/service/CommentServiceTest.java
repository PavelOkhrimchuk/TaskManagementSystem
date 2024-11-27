package org.ohrim.taskmanagementsystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ohrim.taskmanagementsystem.entity.Comment;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.exception.task.ResourceNotFoundException;
import org.ohrim.taskmanagementsystem.repository.CommentRepository;
import org.ohrim.taskmanagementsystem.repository.TaskRepository;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("Test add comment - success")
    void testAddCommentSuccess() {

        Long taskId = 1L;
        String content = "This is a comment";
        String authorEmail = "author@example.com";
        Task task = Task.builder().id(taskId).build();
        User author = User.builder().email(authorEmail).build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(authorEmail)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Comment comment = commentService.addComment(taskId, content, authorEmail);


        assertNotNull(comment);
        assertEquals(content, comment.getContent());
        assertEquals(task, comment.getTask());
        assertEquals(author, comment.getAuthor());
        assertNotNull(comment.getCreatedAt());
        verify(taskRepository).findById(taskId);
        verify(userRepository).findByEmail(authorEmail);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("Test add comment - task not found")
    void testAddCommentTaskNotFound() {

        Long taskId = 1L;
        String content = "This is a comment";
        String authorEmail = "author@example.com";
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> commentService.addComment(taskId, content, authorEmail));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test add comment - author not found")
    void testAddCommentAuthorNotFound() {

        Long taskId = 1L;
        String content = "This is a comment";
        String authorEmail = "author@example.com";
        Task task = Task.builder().id(taskId).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(authorEmail)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> commentService.addComment(taskId, content, authorEmail));
        assertEquals("Author not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test get comments by task - success")
    void testGetCommentsByTaskSuccess() {

        Long taskId = 1L;
        int page = 0;
        int size = 10;
        Page<Comment> commentsPage = mock(Page.class);
        when(commentRepository.findAllByTaskId(taskId, PageRequest.of(page, size))).thenReturn(commentsPage);


        Page<Comment> result = commentService.getCommentsByTask(taskId, page, size);


        assertEquals(commentsPage, result);
        verify(commentRepository).findAllByTaskId(taskId, PageRequest.of(page, size));
    }



}