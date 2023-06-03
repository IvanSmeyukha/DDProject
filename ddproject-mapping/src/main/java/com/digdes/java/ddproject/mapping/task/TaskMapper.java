package com.digdes.java.ddproject.mapping.task;

import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.project.ProjectMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final MemberMapper memberMapper;
    private final ProjectMapper projectMapper;

    public Task fromTaskDto(BaseTaskDto taskDto){
        Task task = Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .creationDate(taskDto.getCreationDate())
                .deadline(taskDto.getDeadline())
                .laborHours(taskDto.getLaborHours())
                .lastUpdateDate(taskDto.getLastUpdateDate())
                .status(taskDto.getStatus())
                .build();
        if(taskDto instanceof ExtTaskDto extTaskDto){
            task.setAuthor(memberMapper.fromMemberDto(extTaskDto.getAuthor()));
            if(!ObjectUtils.isEmpty(taskDto.getExecutorId())) {
                task.setExecutor(memberMapper.fromMemberDto(extTaskDto.getExecutor()));
            }
            task.setProject(projectMapper.fromProjectDto(extTaskDto.getProject()));
        } else {
            task.setAuthor(Member.builder().id(taskDto.getAuthorId()).build());
            if(!ObjectUtils.isEmpty(taskDto.getExecutorId())){
                task.setExecutor(Member.builder().id(taskDto.getExecutorId()).build());
            }
            task.setProject(Project.builder().id(taskDto.getProjectId()).build());
        }
        return task;
    }

    public BaseTaskDto toBaseTaskDto(Task task){
        BaseTaskDto taskDto = BaseTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate())
                .deadline(task.getDeadline())
                .laborHours(task.getLaborHours())
                .lastUpdateDate(task.getLastUpdateDate())
                .status(task.getStatus())
                .authorId(task.getAuthor().getId())
                .projectId(task.getProject().getId())
                .build();
        if(!ObjectUtils.isEmpty(task.getExecutor())){
            taskDto.setExecutorId(task.getExecutor().getId());
        }
        return taskDto;
    }

    public ExtTaskDto toExtTaskDto(Task task){
        ExtTaskDto taskDto = ExtTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate())
                .deadline(task.getDeadline())
                .laborHours(task.getLaborHours())
                .lastUpdateDate(task.getLastUpdateDate())
                .status(task.getStatus())
                .authorId(task.getAuthor().getId())
                .projectId(task.getProject().getId())
                .author(memberMapper.toMemberDto(task.getAuthor()))
                .project(projectMapper.toProjectDto(task.getProject()))
                .build();
        if(!ObjectUtils.isEmpty(task.getExecutor())){
            taskDto.setExecutor(memberMapper.toMemberDto(task.getExecutor()));
            taskDto.setExecutorId(task.getExecutor().getId());
        }
        return taskDto;
    }
}
