package org.ohrim.taskmanagementsystem.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentPageResponse {
    private List<CommentResponse> content;
    private PageableCommentInfo pageable;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private boolean first;
    private boolean last;
}
