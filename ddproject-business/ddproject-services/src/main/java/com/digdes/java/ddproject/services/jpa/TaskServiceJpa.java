package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.common.exceptions.ImpossibleDeadlineException;
import com.digdes.java.ddproject.common.exceptions.MemberNotInProjectException;
import com.digdes.java.ddproject.dto.filters.SearchTaskFilter;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.task.TaskMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Task;
import com.digdes.java.ddproject.repositories.jpa.TaskRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.TaskSpecification;
import com.digdes.java.ddproject.services.ProjectTeamService;
import com.digdes.java.ddproject.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceJpa implements TaskService {
    private final TaskRepositoryJpa taskRepository;
    private final ProjectTeamService projectTeamService;
    private final MemberServiceJpa memberServiceJpa;
    private final TaskMapper taskMapper;
    private final MemberMapper memberMapper;

    @Transactional
    @Override
    public ExtTaskDto create(BaseTaskDto dto) {
        Task task = taskMapper.fromTaskDto(dto);
        task.setCreationDate(OffsetDateTime.now());
        if (task.getCreationDate().isAfter(task.getDeadline().minusHours(task.getLaborHours()))) {
            throw new ImpossibleDeadlineException("Impossible deadline");
        }
//        Или id автора приходит от клиента?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member author = memberMapper.fromMemberDto(memberServiceJpa.findByAccountUsername(authentication.getName()));
        task.setAuthor(author);
        checkMember(task.getProject().getId(), task.getAuthor().getId());
        if (!ObjectUtils.isEmpty(task.getExecutor().getId())){
            checkMember(task.getProject().getId(), task.getAuthor().getId());
        }
        task.setStatus(TaskStatus.NEW);
        return taskMapper.toExtTaskDto(taskRepository.save(task));
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
        Task task = taskMapper.fromTaskDto(dto);
        task.setId(id);
        task.setLastUpdateDate(OffsetDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member author = memberMapper.fromMemberDto(memberServiceJpa.findByAccountUsername(authentication.getName()));
        task.setAuthor(author);
        checkMember(task.getProject().getId(), task.getAuthor().getId());
        if (taskRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException(String.format("Task with id = %d not exists", id));
        }
        if (!ObjectUtils.isEmpty(task.getExecutor().getId())){
            checkMember(task.getProject().getId(), task.getAuthor().getId());
        }
        return taskMapper.toExtTaskDto(taskRepository.save(task));
    }

    @Override
    public ExtTaskDto updateStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Task with id = %d not exists", id))
        );
        task.setStatus(status);
        return taskMapper.toExtTaskDto(taskRepository.save(task));
    }

    @Override
    public List<BaseTaskDto> search(SearchTaskFilter filter) {
        List<Task> tasks = taskRepository.findAll(TaskSpecification.getSpec(filter));
        return tasks
                .stream()
                .map(taskMapper::toBaseTaskDto)
                .toList();
    }
}
