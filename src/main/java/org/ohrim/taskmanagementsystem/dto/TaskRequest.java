package org.ohrim.taskmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohrim.taskmanagementsystem.entity.task.Priority;

@Getter
@Setter
@NoArgsConstructor
public class TaskRequest {

    @NotNull
    @Size(min = 3, max = 255)
    private String title;

    @NotNull
    @Size(min = 10, max = 1000)
    private String description;

    @NotNull
    private Priority priority;

    private String executorEmail;
}
