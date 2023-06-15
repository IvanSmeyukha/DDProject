package com.digdes.java.ddproject.services.unit.jpa;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.common.exceptions.ImpossibleDeadlineException;
import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.mapping.filters.SearchTaskFilterMapper;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.project.ProjectMapper;
import com.digdes.java.ddproject.mapping.task.TaskMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Task;
import com.digdes.java.ddproject.repositories.jpa.TaskRepositoryJpa;
import com.digdes.java.ddproject.services.ProjectTeamService;
import com.digdes.java.ddproject.services.jpa.MemberServiceJpa;
import com.digdes.java.ddproject.services.jpa.ProjectServiceJpa;
import com.digdes.java.ddproject.services.jpa.TaskServiceJpa;
import com.digdes.java.ddproject.services.notification.Notifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceJpaTest {
    @Spy
    private TaskRepositoryJpa taskRepository;
    @Mock
    private ProjectTeamService projectTeamService;
    @Mock
    private MemberServiceJpa memberService;
    @Mock
    private ProjectServiceJpa projectService;
    private TaskMapper taskMapper;
    @Spy
    private MemberMapper memberMapper;
    @Spy
    private SearchTaskFilterMapper filterMapper;
    @Spy
    private ProjectMapper projectMapper;
    @Mock
    private Notifier notifier;

    private TaskServiceJpa taskService;

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        taskMapper = Mockito.spy(new TaskMapper(memberMapper, projectMapper));
        taskService = new TaskServiceJpa(
                taskRepository,
                projectTeamService,
                memberService,
                projectService,
                taskMapper,
                memberMapper,
                filterMapper,
                notifier);
    }

    private MemberDto createMemberDto() {
        return MemberDto.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .account(1L)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    private Long getRandomLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
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
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L).build();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(projectService.isProjectExist(any())).thenReturn(true);
        when(memberService.findByAccountUsername(any())).thenReturn(new MemberDto());

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.create(task));
    }

    @Test
    @WithMockUser(username = "root")
    void create_AuthorNotInProject_Exception() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), any())).thenReturn(false);
        when(projectService.isProjectExist(any())).thenReturn(true);
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
        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        MemberDto executor = createMemberDto();
        executor.setId(getRandomLong());
        when(projectTeamService.isMemberInProject(any(), eq(executor.getId()))).thenReturn(false);
        when(projectService.isProjectExist(any())).thenReturn(true);
        when(memberService.isMemberExist(any())).thenReturn(true);

        BaseTaskDto task = BaseTaskDto.builder()
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
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), eq(author.getId()))).thenReturn(true);
        when(projectService.isProjectExist(any())).thenReturn(true);
        BaseTaskDto taskDto = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .build();
        Task task = taskMapper.fromTaskDto(taskDto);
        when(taskRepository.save(any())).thenReturn(task);

        ExtTaskDto createdTask = taskService.create(taskDto);

        Assertions.assertEquals(task.getTitle(), createdTask.getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void create_AuthorAndExecutorInProject_TaskCreated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        MemberDto executor = createMemberDto();
        executor.setId(getRandomLong());
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), any())).thenReturn(true);
        when(projectService.isProjectExist(any())).thenReturn(true);
        when(memberService.isMemberExist(any())).thenReturn(true);
        when(memberService.findById(any())).thenReturn(new MemberDto());
        BaseTaskDto taskDto = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .projectId(projectId)
                .executorId(executor.getId())
                .build();
        Task task = taskMapper.fromTaskDto(taskDto);
        when(taskRepository.save(any())).thenReturn(task);

        ExtTaskDto createdTask = taskService.create(taskDto);

        Assertions.assertEquals(task.getTitle(), createdTask.getTitle());
    }

    @Test
    void update_TaskWithSuchIdNotExist_Exception() {
        BaseTaskDto task = BaseTaskDto.builder().build();
        Long taskId = getRandomLong();
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.update(taskId, task));
    }

    @Test
    void update_GivenImpossibleDeadline_Exception() {
        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now())
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .build();
        Long taskId = getRandomLong();
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));

        Assertions.assertThrows(ImpossibleDeadlineException.class, () -> taskService.update(taskId, task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_UserNotMember_Exception() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .build();
        Long taskId = getRandomLong();
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
        when(memberService.findByAccountUsername(any())).thenReturn(new MemberDto());
        when(projectService.isProjectExist(any())).thenReturn(true);

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(taskId, task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_AuthorNotInProject_Exception() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long taskId = getRandomLong();
        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), any())).thenReturn(false);
        when(projectService.isProjectExist(any())).thenReturn(true);

        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .projectId(projectId)
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(taskId, task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_ExecutorNotInProject_Exception() {
        Long taskId = getRandomLong();
        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        MemberDto executor = createMemberDto();
        executor.setId(getRandomLong());
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
        when(projectService.isProjectExist(any())).thenReturn(true);
        when(projectTeamService.isMemberInProject(any(), eq(executor.getId()))).thenReturn(false);
        when(memberService.isMemberExist(any())).thenReturn(true);

        BaseTaskDto task = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .projectId(projectId)
                .executorId(executor.getId())
                .build();

        Assertions.assertThrows(MemberNotInProjectException.class, () -> taskService.update(taskId, task));
    }

    @Test
    @WithMockUser(username = "root")
    void update_ExecutorNotGiven_TaskCreated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long taskId = getRandomLong();
        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), eq(author.getId()))).thenReturn(true);
        when(projectService.isProjectExist(any())).thenReturn(true);

        BaseTaskDto taskDto = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .projectId(projectId)
                .build();
        Task task = taskMapper.fromTaskDto(taskDto);
        when(taskRepository.save(any())).thenReturn(task);

        ExtTaskDto createdTask = taskService.update(taskId, taskDto);

        Assertions.assertEquals(task.getTitle(), createdTask.getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void update_AuthorAndExecutorInProject_TaskCreated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long taskId = getRandomLong();
        Long projectId = getRandomLong();
        MemberDto author = createMemberDto();
        author.setId(getRandomLong());
        MemberDto executor = createMemberDto();
        executor.setId(getRandomLong());
        when(taskRepository.findById(any())).thenReturn(Optional.of(new Task()));
        when(memberService.findByAccountUsername(any())).thenReturn(author);
        when(projectTeamService.isMemberInProject(any(), any())).thenReturn(true);
        when(projectService.isProjectExist(any())).thenReturn(true);
        when(memberService.isMemberExist(any())).thenReturn(true);
        when(memberService.findById(any())).thenReturn(new MemberDto());

        BaseTaskDto taskDto = BaseTaskDto.builder()
                .deadline(OffsetDateTime.now().plusDays(2L))
                .laborHours(1L)
                .creationDate(OffsetDateTime.now())
                .projectId(projectId)
                .executorId(executor.getId())
                .build();
        Task task = taskMapper.fromTaskDto(taskDto);
        when(taskRepository.save(any())).thenReturn(task);

        ExtTaskDto createdTask = taskService.update(taskId, taskDto);

        Assertions.assertEquals(task.getTitle(), createdTask.getTitle());
    }

    @Test
    @WithMockUser(username = "root")
    void updateStatus_TaskWithSuchIdNotExist_Exception() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.updateStatus(getRandomLong(), TaskStatus.CLOSED));
    }
}
