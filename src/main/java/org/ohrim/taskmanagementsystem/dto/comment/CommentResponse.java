package org.ohrim.taskmanagementsystem.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private AuthorResponse author;
    private TaskSummaryResponse task;
    private Instant createdAt;
}
