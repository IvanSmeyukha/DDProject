package com.digdes.java.ddproject.mapping.project;

import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project fromProjectDto(ProjectDto projectDto){
        return Project.builder()
                .id(projectDto.getId())
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .status(projectDto.getStatus())
                .build();
    }

    public ProjectDto toProjectDto(Project project){
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .status(project.getStatus())
                .build();
    }
}
