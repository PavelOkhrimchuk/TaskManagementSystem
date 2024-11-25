package org.ohrim.taskmanagementsystem.mapper;

import org.ohrim.taskmanagementsystem.dto.comment.*;
import org.ohrim.taskmanagementsystem.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentResponse toCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());


        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(comment.getAuthor().getId());
        authorResponse.setName(comment.getAuthor().getName());
        commentResponse.setAuthor(authorResponse);


        TaskSummaryResponse taskSummaryResponse = new TaskSummaryResponse();
        taskSummaryResponse.setId(comment.getTask().getId());
        taskSummaryResponse.setTitle(comment.getTask().getTitle());
        taskSummaryResponse.setStatus(comment.getTask().getStatus());
        commentResponse.setTask(taskSummaryResponse);

        return commentResponse;
    }



    public CommentPageResponse toCommentPageResponse(Page<Comment> commentPage) {
        CommentPageResponse response = new CommentPageResponse();


        List<CommentResponse> content = commentPage.getContent()
                .stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
        response.setContent(content);


        PageableCommentInfo pageable = new PageableCommentInfo();
        pageable.setPageNumber(commentPage.getNumber());
        pageable.setPageSize(commentPage.getSize());
        response.setPageable(pageable);


        response.setTotalPages(commentPage.getTotalPages());
        response.setTotalElements(commentPage.getTotalElements());
        response.setNumberOfElements(commentPage.getNumberOfElements());
        response.setFirst(commentPage.isFirst());
        response.setLast(commentPage.isLast());

        return response;
    }
}
