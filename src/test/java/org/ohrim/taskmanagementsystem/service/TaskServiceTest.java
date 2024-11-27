package org.ohrim.taskmanagementsystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ohrim.taskmanagementsystem.dto.task.TaskRequest;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.ohrim.taskmanagementsystem.entity.User;
import org.ohrim.taskmanagementsystem.entity.task.Priority;
import org.ohrim.taskmanagementsystem.entity.task.Status;
import org.ohrim.taskmanagementsystem.exception.task.ResourceNotFoundException;
import org.ohrim.taskmanagementsystem.exception.task.TaskStatusException;
import org.ohrim.taskmanagementsystem.repository.TaskRepository;
import org.ohrim.taskmanagementsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Test create task - success")
    void testCreateTaskSuccess() {

        TaskRequest taskRequest = new TaskRequest("Task Title", "Task Description", Priority.HIGH, null);
        String authorEmail = "author@example.com";
        User author = new User();
        author.setEmail(authorEmail);
        Task task = new Task();

        when(userRepository.findByEmail(authorEmail)).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenReturn(task);


        Task createdTask = taskService.createTask(taskRequest, authorEmail);


        assertNotNull(createdTask);
        assertEquals(task, createdTask);
    }

    @Test
    @DisplayName("Test create task - author not found")
    void testCreateTaskAuthorNotFound() {

        TaskRequest taskRequest = new TaskRequest("Task Title", "Task Description", Priority.HIGH, null);
        String authorEmail = "author@example.com";

        when(userRepository.findByEmail(authorEmail)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(taskRequest, authorEmail));
        assertEquals("Author not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test create task - executor not found")
    void testCreateTaskExecutorNotFound() {

        TaskRequest taskRequest = new TaskRequest("Task Title", "Task Description", Priority.HIGH, "executor@example.com");
        String authorEmail = "author@example.com";
        User author = new User();
        author.setEmail(authorEmail);
        User executor = new User();
        executor.setEmail("executor@example.com");

        when(userRepository.findByEmail(authorEmail)).thenReturn(Optional.of(author));
        when(userRepository.findByEmail("executor@example.com")).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(taskRequest, authorEmail));
        assertEquals("Executor not found", exception.getMessage());
    }


    @Test
    @DisplayName("Test update task - success")
    void testUpdateTaskSuccess() {

        Long taskId = 1L;
        TaskRequest taskRequest = new TaskRequest("Updated Task", "Updated Description", Priority.MEDIUM, "executor@example.com");
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");

        User executor = new User();
        executor.setEmail("executor@example.com");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findByEmail("executor@example.com")).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);


        Task updatedTask = taskService.updateTask(taskId, taskRequest);


        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTitle());
    }

    @Test
    @DisplayName("Test update task - task not found")
    void testUpdateTaskTaskNotFound() {

        Long taskId = 1L;
        TaskRequest taskRequest = new TaskRequest("Updated Task", "Updated Description", Priority.MEDIUM, "executor@example.com");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, taskRequest));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test update task - executor not found")
    void testUpdateTaskExecutorNotFound() {

        Long taskId = 1L;
        TaskRequest taskRequest = new TaskRequest("Updated Task", "Updated Description", Priority.MEDIUM, "executor@example.com");
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findByEmail("executor@example.com")).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, taskRequest));
        assertEquals("Executor not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test delete task - success")
    void testDeleteTaskSuccess() {

        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);


        taskService.deleteTask(taskId);


        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Test delete task - task not found")
    void testDeleteTaskNotFound() {

        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(taskId));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test change task status - success")
    void testChangeTaskStatusSuccess() {
        // Given
        Long taskId = 1L;
        Status newStatus = Status.COMPLETED;
        String userEmail = "user@example.com";
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);


        Task updatedTask = taskService.changeStatus(taskId, newStatus, userEmail);


        assertNotNull(updatedTask);
        assertEquals(newStatus, updatedTask.getStatus());
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Test change task status - task not found")
    void testChangeTaskStatusTaskNotFound() {

        Long taskId = 1L;
        Status newStatus = Status.COMPLETED;
        String userEmail = "user@example.com";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.changeStatus(taskId, newStatus, userEmail));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test change task status - invalid status")
    void testChangeTaskStatusInvalidStatus() {

        Long taskId = 1L;
        String userEmail = "user@example.com";
        Task existingTask = new Task();
        existingTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));


        TaskStatusException exception = assertThrows(TaskStatusException.class, () -> taskService.changeStatus(taskId, null, userEmail));
        assertEquals("Invalid task status", exception.getMessage());
    }

    @Test
    @DisplayName("Test assign executor - success")
    void testAssignExecutorSuccess() {

        Long taskId = 1L;
        String executorEmail = "executor@example.com";
        Task existingTask = new Task();
        existingTask.setId(taskId);

        User executor = new User();
        executor.setEmail(executorEmail);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findByEmail(executorEmail)).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);


        Task updatedTask = taskService.assignExecutor(taskId, executorEmail);


        assertNotNull(updatedTask);
        assertEquals(executor, updatedTask.getExecutor());
    }

    @Test
    @DisplayName("Test assign executor - task not found")
    void testAssignExecutorTaskNotFound() {

        Long taskId = 1L;
        String executorEmail = "executor@example.com";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.assignExecutor(taskId, executorEmail));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test assign executor - executor not found")
    void testAssignExecutorExecutorNotFound() {

        Long taskId = 1L;
        String executorEmail = "executor@example.com";
        Task existingTask = new Task();
        existingTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findByEmail(executorEmail)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> taskService.assignExecutor(taskId, executorEmail));
        assertEquals("Executor not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test get tasks by author - success")
    void testGetTasksByAuthorSuccess() {

        String authorEmail = "author@example.com";
        int page = 0;
        int size = 10;
        Page<Task> tasksPage = new PageImpl<>(List.of(new Task()));

        when(taskRepository.findAllByAuthorEmail(authorEmail, PageRequest.of(page, size))).thenReturn(tasksPage);


        Page<Task> result = taskService.getTasksByAuthor(authorEmail, page, size);


        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("Test get tasks by executor - success")
    void testGetTasksByExecutorSuccess() {

        String executorEmail = "executor@example.com";
        int page = 0;
        int size = 10;
        Page<Task> tasksPage = new PageImpl<>(List.of(new Task()));

        when(taskRepository.findAllByExecutorEmail(executorEmail, PageRequest.of(page, size))).thenReturn(tasksPage);


        Page<Task> result = taskService.getTasksByExecutor(executorEmail, page, size);


        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }





}