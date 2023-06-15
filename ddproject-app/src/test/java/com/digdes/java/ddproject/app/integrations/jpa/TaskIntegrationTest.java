package com.digdes.java.ddproject.app.integrations.jpa;

import com.digdes.java.ddproject.app.MainApp;
import com.digdes.java.ddproject.app.integrations.IntegrationEnvironment;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.common.exceptions.ImpossibleDeadlineException;
import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.dto.filters.SearchTaskFilterDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.model.*;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectTeamRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.TaskRepositoryJpa;
import com.digdes.java.ddproject.services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = MainApp.class)
class TaskIntegrationTest extends IntegrationEnvironment {
    @Autowired
    TaskRepositoryJpa taskRepository;

    @Autowired
    MemberRepositoryJpa memberRepository;

    @Autowired
    ProjectTeamRepositoryJpa projectTeamRepository;

    @Autowired
    ProjectRepositoryJpa projectRepository;

    @Autowired
    TaskService taskService;

    private Task saveRandomTask() {
        Member author = createMember();
        Long authorId = memberRepository.save(author).getId();
        author.setId(authorId);
        Member executor = createMember();
        Long executorId = memberRepository.save(executor).getId();
        executor.setId(executorId);
        Long projectId = getRandomLong();
        Project project = createProject(projectId);
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        projectTeamRepository.save(new ProjectTeam(projectId, executorId, Role.TESTER));
        Task task = Task.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .project(project)
                .author(author)
                .executor(executor)
                .status(TaskStatus.NEW)
                .build();

        return taskRepository.save(task);
    }

    @Test
    void create_GivenImpossibleDeadline_Exception() {
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now())
                .laborHours(1L).build();

        Assertions.assertThrows(ImpossibleDeadlineException.class, () -> taskService.create(task));
    }

    @Test
    @WithMockUser(username = "root")
    void create_UserNotMember_Exception() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.create(task));
    }

    @Test
    @WithMockUser(username = "root")
    void create_AuthorNotInProject_Exception() {
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        memberRepository.save(author);
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.create(task));
    }

    @Test
    @WithMockUser(username = "root")
    void create_ExecutorNotInProject_Exception() {
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Member executor = createMember();
        Long executorId = memberRepository.save(executor).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .executorId(executor.getId())
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.create(task));
    }

    @Test
    @WithMockUser(username = "root")
    void create_ExecutorNotGiven_TaskCreated() {
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .build();

        ExtTaskDto createdTask = taskService.create(task);

        Optional<Task> foundTask = taskRepository.findById(createdTask.getId());
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task.getTitle(), foundTask.get().getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void create_AuthorAndExecutorInProject_TaskCreated() {
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Member executor = createMember();
        Long executorId = memberRepository.save(executor).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        projectTeamRepository.save(new ProjectTeam(projectId, executorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .executorId(executorId)
                .build();

        ExtTaskDto createdTask = taskService.create(task);

        Optional<Task> foundTask = taskRepository.findById(createdTask.getId());
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task.getTitle(), foundTask.get().getTitle());
    }

    @Test
    void update_TaskWithSuchIdNotExist_Exception() {
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now())
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .build();

        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.update(getRandomLong(), task));
    }

    @Test
    void update_GivenImpossibleDeadline_Exception() {
        Task originaltask = saveRandomTask();
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now())
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .build();

        Assertions.assertThrows(ImpossibleDeadlineException.class, () -> taskService.update(originaltask.getId(), task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_UserNotMember_Exception() {
        Task originaltask = saveRandomTask();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .projectId(projectId)
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(originaltask.getId(), task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_AuthorNotInProject_Exception() {
        Task originaltask = saveRandomTask();
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        memberRepository.save(author);
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .creationDate(OffsetDateTime.now())
                .laborHours(1L)
                .projectId(projectId)
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(originaltask.getId(), task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_ExecutorNotInProject_Exception() {
        Task originaltask = saveRandomTask();
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Member executor = createMember();
        Long executorId = memberRepository.save(executor).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .creationDate(OffsetDateTime.now())
                .laborHours(1L)
                .projectId(projectId)
                .executorId(executor.getId())
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(originaltask.getId(), task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_ExecutorNotGiven_TaskUpdated() {
        Task originaltask = saveRandomTask();
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .creationDate(OffsetDateTime.now())
                .laborHours(1L)
                .projectId(projectId)
                .status(TaskStatus.NEW)
                .build();

        ExtTaskDto createdTask = taskService.update(originaltask.getId(), task);

        Optional<Task> foundTask = taskRepository.findById(createdTask.getId());
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task.getTitle(), foundTask.get().getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void update_AuthorAndExecutorInProject_TaskUpdated() {
        Task originaltask = saveRandomTask();
        Member author = createMember();
        author.setAccount(UserAccount.builder().id(1L).build());
        Long authorId = memberRepository.save(author).getId();
        Member executor = createMember();
        Long executorId = memberRepository.save(executor).getId();
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        projectTeamRepository.save(new ProjectTeam(projectId, authorId, Role.TESTER));
        projectTeamRepository.save(new ProjectTeam(projectId, executorId, Role.TESTER));
        BaseTaskDto task = BaseTaskDto.builder()
                .title(UUID.randomUUID().toString())
                .deadline(OffsetDateTime.now().plusDays(2L))
                .creationDate(OffsetDateTime.now())
                .laborHours(1L)
                .projectId(projectId)
                .executorId(executorId)
                .status(TaskStatus.NEW)
                .build();

        ExtTaskDto createdTask = taskService.update(originaltask.getId(), task);

        Optional<Task> foundTask = taskRepository.findById(createdTask.getId());
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(task.getTitle(), foundTask.get().getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void updateStatus_TaskWithSuchIdNotExist_Exception() {

        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.updateStatus(getRandomLong(), TaskStatus.CLOSED));
    }

    @Test
    void updateStatus_TaskWithSuchIdExist_TaskStatusUpdated() {
        Long taskId = saveRandomTask().getId();

        taskService.updateStatus(taskId, TaskStatus.CLOSED);

        Optional<Task> foundTask = taskRepository.findById(taskId);
        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(TaskStatus.CLOSED, foundTask.get().getStatus());
    }

    @Test
    void search_SearchByTitle_ReturnTaskList() {
        Task targetTask = saveRandomTask();

        SearchTaskFilterDto filter = new SearchTaskFilterDto();
        filter.setTitle(targetTask.getTitle());
        List<BaseTaskDto> tasks = taskService.search(filter);

        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(tasks.get(0).getTitle(), targetTask.getTitle());
    }

}
