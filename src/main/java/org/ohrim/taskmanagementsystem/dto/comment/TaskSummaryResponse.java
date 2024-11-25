package org.ohrim.taskmanagementsystem.dto.comment;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohrim.taskmanagementsystem.entity.task.Status;

@Getter
@Setter
@NoArgsConstructor
public class TaskSummaryResponse {

    private Long id;
    private String title;
    private Status status;
}
