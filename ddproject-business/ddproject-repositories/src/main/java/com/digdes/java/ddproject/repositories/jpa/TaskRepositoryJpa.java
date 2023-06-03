package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepositoryJpa extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}
