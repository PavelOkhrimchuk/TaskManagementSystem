package org.ohrim.taskmanagementsystem.dto.task;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedTasksResponse {
    private List<TaskResponse> tasks;
    private PaginationTaskInfo pagination;
}
