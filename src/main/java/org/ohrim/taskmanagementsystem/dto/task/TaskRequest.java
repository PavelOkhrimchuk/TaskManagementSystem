package org.ohrim.taskmanagementsystem.dto.task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohrim.taskmanagementsystem.entity.task.Priority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotNull(message = "Title is required.")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters.")
    private String title;

    @NotNull(message = "Description is required.")
    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters.")
    private String description;

    @NotNull(message = "Priority is required.")
    private Priority priority;

    @Email(message = "Executor email must be valid.")
    private String executorEmail;
}
