package com.digdes.java.ddproject.app.integrations;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.model.ProjectTeam;
import com.digdes.java.ddproject.model.Task;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectTeamRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.TaskRepositoryJpa;
import com.digdes.java.ddproject.services.MemberService;
import com.digdes.java.ddproject.services.ProjectTeamService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

public class IntegrationEnvironment {
    static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres");

    @Autowired
    MemberRepositoryJpa memberRepositoryJpa;
    @Autowired
    ProjectRepositoryJpa projectRepositoryJpa;
    @Autowired
    TaskRepositoryJpa taskRepositoryJpa;
    @Autowired
    ProjectTeamRepositoryJpa projectTeamRepositoryJpa;

    @BeforeAll
    public static void startContainer() {
        POSTGRESQL_CONTAINER.start();
    }

    @AfterAll
    public static void stopContainer() {
        POSTGRESQL_CONTAINER.stop();
    }

    @BeforeEach
    void clearDatabase() {
        memberRepositoryJpa.deleteAll();
        projectRepositoryJpa.deleteAll();
        taskRepositoryJpa.deleteAll();
        projectRepositoryJpa.deleteAll();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    protected Long getRandomLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    protected MemberDto createMemberDto() {
        return MemberDto.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    protected Member createMember() {
        return Member.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    protected ProjectDto createProjectDto() {
        return ProjectDto.builder()
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }

    protected ProjectDto createProjectDto(Long id) {
        return ProjectDto.builder()
                .id(id)
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }

    protected Project createProject(Long id) {
        return Project.builder()
                .id(id)
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }
//
//    protected Task createTask() {
//        return Task.builder()
//                .title(UUID.randomUUID().toString())
//                .laborHours()
//                .deadline()
//                .project()
//                .status(TaskStatus.NEW)
//                .build();
//    }
//
//    protected BaseTaskDto createTaskDto() {
//        return Task.builder()
//                .title(UUID.randomUUID().toString())
//                .laborHours()
//                .deadline()
//                .project()
//                .status(TaskStatus.NEW)
//                .build();
//    }

}
