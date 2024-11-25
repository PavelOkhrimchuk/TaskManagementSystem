package org.ohrim.taskmanagementsystem.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohrim.taskmanagementsystem.entity.task.Priority;
import org.ohrim.taskmanagementsystem.entity.task.Status;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UserResponse author;
    private UserResponse executor;
    private Instant createdAt;
    private Instant updatedAt;
}