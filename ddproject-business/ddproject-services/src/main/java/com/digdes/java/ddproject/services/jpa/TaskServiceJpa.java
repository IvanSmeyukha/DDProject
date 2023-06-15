package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.common.exceptions.ImpossibleDeadlineException;
import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.dto.filters.SearchTaskFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.mapping.filters.SearchTaskFilterMapper;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.task.TaskMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Task;
import com.digdes.java.ddproject.repositories.jpa.TaskRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.specifications.TaskSpecification;
import com.digdes.java.ddproject.services.ProjectTeamService;
import com.digdes.java.ddproject.services.TaskService;
import com.digdes.java.ddproject.services.notification.Notifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceJpa implements TaskService {
    private final TaskRepositoryJpa taskRepository;
    private final ProjectTeamService projectTeamService;
    private final MemberServiceJpa memberService;
    private final ProjectServiceJpa projectService;
    private final TaskMapper taskMapper;
    private final MemberMapper memberMapper;
    private final SearchTaskFilterMapper filterMapper;
    private final Notifier notifier;

    @Transactional
    @Override
    public ExtTaskDto create(BaseTaskDto dto) {
        dto.setCreationDate(OffsetDateTime.now());
        validateTask(dto);
        Task task = taskMapper.fromTaskDto(dto);
        task.setStatus(TaskStatus.NEW);
        Member author = getAuthor(dto);
        task.setAuthor(author);

        Task createdTask = taskRepository.save(task);

        sendNotify(task);

        log.info("Create task with id = {}", createdTask.getId());
        return taskMapper.toExtTaskDto(createdTask);
    }

    @Transactional
    @Override
    public ExtTaskDto update(Long id, BaseTaskDto dto) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException(String.format("Task with id = %d not exists", id));
        }
        validateTask(dto);
        Task task = taskMapper.fromTaskDto(dto);
        task.setId(id);
        task.setLastUpdateDate(OffsetDateTime.now());
        Member author = getAuthor(dto);
        task.setAuthor(author);

        Task updatedTask = taskRepository.save(task);

        sendNotify(task);

        log.info("Update task with id = {}", updatedTask.getId());
        return taskMapper.toExtTaskDto(updatedTask);
    }

    private void validateTask(BaseTaskDto task) {
        if (task.getCreationDate().isAfter(task.getDeadline().minusHours(task.getLaborHours()))) {
            throw new ImpossibleDeadlineException("Impossible deadline");
        }

        Long projectId = task.getProjectId();
        if (!projectService.isProjectExist(projectId)) {
            throw new NoSuchElementException(String.format("Project with id = %d not exist", projectId));
        }

        if (!ObjectUtils.isEmpty(task.getExecutorId())) {
            Long executorId = task.getExecutorId();
            if (!memberService.isMemberExist(executorId)) {
                throw new NoSuchElementException(String.format("Member with id = %d not exist", executorId));
            }
            checkMemberInProject(projectId, executorId);
        }
    }

    private Member getAuthor(BaseTaskDto task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member author = memberMapper.fromMemberDto(memberService.findByAccountUsername(authentication.getName()));
        if (ObjectUtils.isEmpty(author.getId())) {
            throw new MemberNotInProjectException("Only authorized members can create a task! (Authorized user must be member)");
        }
        checkMemberInProject(task.getProjectId(), author.getId());
        return author;
    }

    private void checkMemberInProject(Long projectId, Long memberId) {
        if (!projectTeamService.isMemberInProject(projectId, memberId)) {
            throw new MemberNotInProjectException(
                    String.format("Member with id = %d is not involved in project with id = %d",
                            memberId,
                            projectId)
            );
        }
    }

    private void sendNotify(Task task) {
        if (!ObjectUtils.isEmpty(task.getExecutor())) {
            MemberDto executor = memberService.findById(task.getExecutor().getId());
            if (!ObjectUtils.isEmpty(executor.getEmail())) {
                notifier.sendMessage(executor.getEmail(), executor.getFirstName());
            }
        }
    }


    @Override
    public ExtTaskDto updateStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Task with id = %d not exists", id))
        );
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        log.info("Update the status of the task with id = {}", updatedTask.getId());
        return taskMapper.toExtTaskDto(updatedTask);
    }

    @Override
    public List<BaseTaskDto> search(SearchTaskFilterDto filter) {
        List<Task> tasks = taskRepository.findAll(TaskSpecification.getSpec(filterMapper.fromDto(filter)));
        return tasks
                .stream()
                .map(taskMapper::toBaseTaskDto)
                .toList();
    }
}
