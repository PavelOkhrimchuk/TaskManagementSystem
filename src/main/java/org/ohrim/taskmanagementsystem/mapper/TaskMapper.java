package org.ohrim.taskmanagementsystem.mapper;

import org.ohrim.taskmanagementsystem.dto.task.PaginatedTasksResponse;
import org.ohrim.taskmanagementsystem.dto.task.PaginationTaskInfo;
import org.ohrim.taskmanagementsystem.dto.task.TaskResponse;
import org.ohrim.taskmanagementsystem.dto.task.UserResponse;
import org.ohrim.taskmanagementsystem.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setCreatedAt(task.getCreatedAt());
        taskResponse.setUpdatedAt(task.getUpdatedAt());


        UserResponse authorResponse = new UserResponse();
        authorResponse.setId(task.getAuthor().getId());
        authorResponse.setName(task.getAuthor().getName());
        authorResponse.setEmail(task.getAuthor().getEmail());
        authorResponse.setRole(task.getAuthor().getRole());
        taskResponse.setAuthor(authorResponse);


        if (task.getExecutor() != null) {
            UserResponse executorResponse = new UserResponse();
            executorResponse.setId(task.getExecutor().getId());
            executorResponse.setName(task.getExecutor().getName());
            executorResponse.setEmail(task.getExecutor().getEmail());
            executorResponse.setRole(task.getExecutor().getRole());
            taskResponse.setExecutor(executorResponse);
        }

        return taskResponse;
    }


    public PaginatedTasksResponse toPaginatedTasksResponse(Page<Task> tasksPage) {
        PaginatedTasksResponse response = new PaginatedTasksResponse();


        List<TaskResponse> taskResponses = tasksPage.getContent().stream()
                .map(this::toTaskResponse)
                .collect(Collectors.toList());

        response.setTasks(taskResponses);


        PaginationTaskInfo paginationInfo = new PaginationTaskInfo();
        paginationInfo.setCurrentPage(tasksPage.getNumber());
        paginationInfo.setPageSize(tasksPage.getSize());
        paginationInfo.setTotalPages(tasksPage.getTotalPages());
        paginationInfo.setTotalTasks(tasksPage.getTotalElements());

        response.setPagination(paginationInfo);
        return response;
    }
}
