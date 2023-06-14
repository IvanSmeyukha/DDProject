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
import com.digdes.java.ddproject.repositories.jpa.TaskSpecification;
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
    private final MemberServiceJpa memberServiceJpa;
    private final TaskMapper taskMapper;
    private final MemberMapper memberMapper;
    private final SearchTaskFilterMapper filterMapper;
    private final Notifier notifier;

    @Transactional
    @Override
    public ExtTaskDto create(BaseTaskDto dto) {
        Task task = taskMapper.fromTaskDto(dto);
        task.setCreationDate(OffsetDateTime.now());
        validateTask(task);
        task.setStatus(TaskStatus.NEW);
        Task createdTask = taskRepository.save(task);
        MemberDto executor = memberServiceJpa.findById(task.getExecutor().getId());
        if(!ObjectUtils.isEmpty(executor.getEmail())){
            notifier.sendMessage(executor.getEmail(), executor.getFirstName());
        }
        log.info("Create task with id = {}", createdTask.getId());
        return taskMapper.toExtTaskDto(createdTask);
    }

    private void validateTask(Task task) {
        if (task.getCreationDate().isAfter(task.getDeadline().minusHours(task.getLaborHours()))) {
            throw new ImpossibleDeadlineException("Impossible deadline");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member author = memberMapper.fromMemberDto(memberServiceJpa.findByAccountUsername(authentication.getName()));
        if(ObjectUtils.isEmpty(author.getId())) {
            throw new MemberNotInProjectException("Only a project members can create a task");
        }
        task.setAuthor(author);
        checkMember(task.getProject().getId(), task.getAuthor().getId());
        if (!ObjectUtils.isEmpty(task.getExecutor())){
            checkMember(task.getProject().getId(), task.getExecutor().getId());
        }
    }

    private void checkMember(Long projectId, Long memberId) {
        if (!projectTeamService.checkMember(projectId, memberId)) {
            throw new MemberNotInProjectException(
                    String.format("Member with id = %d not involved in project with id = %d",
                            projectId,
                            memberId)
            );
        }
    }

    @Transactional
    @Override
    public ExtTaskDto update(Long id, BaseTaskDto dto) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException(String.format("Task with id = %d not exists", id));
        }
        Task task = taskMapper.fromTaskDto(dto);
        validateTask(task);
        task.setId(id);
        task.setLastUpdateDate(OffsetDateTime.now());
        Task updatedTask = taskRepository.save(task);
        MemberDto executor = memberServiceJpa.findById(task.getExecutor().getId());
        if(!ObjectUtils.isEmpty(executor.getEmail())){
            notifier.sendMessage(executor.getEmail(), executor.getFirstName());
        }
        log.info("Update task with id = {}", updatedTask.getId());
        return taskMapper.toExtTaskDto(updatedTask);
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
