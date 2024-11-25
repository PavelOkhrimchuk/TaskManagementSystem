package org.ohrim.taskmanagementsystem.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationTaskInfo {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalTasks;

}
