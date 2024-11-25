package org.ohrim.taskmanagementsystem.repository;

import org.ohrim.taskmanagementsystem.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


    Page<Task> findAllByAuthorEmail(String authorEmail, Pageable pageable);

    Page<Task> findAllByExecutorEmail(String executorEmail, Pageable pageable);
}
