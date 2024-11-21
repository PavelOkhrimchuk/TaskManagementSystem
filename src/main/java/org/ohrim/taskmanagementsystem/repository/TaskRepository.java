package org.ohrim.taskmanagementsystem.repository;

import org.ohrim.taskmanagementsystem.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
