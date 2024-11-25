package org.ohrim.taskmanagementsystem.repository;

import org.ohrim.taskmanagementsystem.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByTaskId(Long taskId);

    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);

}
