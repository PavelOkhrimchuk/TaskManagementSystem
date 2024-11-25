package org.ohrim.taskmanagementsystem.dto.comment;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageableCommentInfo {
    private int pageNumber;
    private int pageSize;
}
