package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepositoryJpa extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
