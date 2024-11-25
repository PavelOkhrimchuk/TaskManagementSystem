package org.ohrim.taskmanagementsystem.dto.comment;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSummaryResponse {

    private Long id;
    private String title;
    private String status;
}
