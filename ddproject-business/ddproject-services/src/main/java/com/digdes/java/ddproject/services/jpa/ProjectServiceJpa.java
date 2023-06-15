package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.mapping.filters.SearchMemberFilterMapper;
import com.digdes.java.ddproject.mapping.filters.SearchProjectFilterMapper;
import com.digdes.java.ddproject.mapping.project.ProjectMapper;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectSpecification;
import com.digdes.java.ddproject.services.ProjectService;
import com.digdes.java.ddproject.services.ProjectTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceJpa implements ProjectService {
    private final ProjectRepositoryJpa projectRepository;
    private final ProjectTeamService projectTeamService;
    private final MemberServiceJpa memberService;
    private final ProjectMapper projectMapper;
    private final SearchProjectFilterMapper filterMapper;

    @Transactional
    @Override
    public ProjectDto create(ProjectDto dto) {
        Project project = projectMapper.fromProjectDto(dto);
        if(projectRepository.findById(project.getId()).isPresent()){
            throw new DuplicateKeyException(String.format("Project with id = %d already exists", project.getId()));
        }
        project.setStatus(ProjectStatus.DRAFT);
        Project createdProject = projectRepository.save(project);
        log.info("Create project with id = {}", createdProject.getId());
        return projectMapper.toProjectDto(createdProject);
    }

    @Transactional
    @Override
    public ProjectDto update(Long id, ProjectDto dto) {
        if(projectRepository.findById(id).isEmpty()){
            throw new NoSuchElementException(String.format("Project with id = %d not exists", id));
        }
        Project project = projectMapper.fromProjectDto(dto);
        project.setId(id);
        Project updatedProject = projectRepository.save(project);
        log.info("Update project with id = {}", updatedProject.getId());
        return projectMapper.toProjectDto(updatedProject);
    }

    @Transactional
    @Override
    public ProjectDto updateStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Project with id = %d not exists", id))
        );
        project.setStatus(status);
        Project updatedProject = projectRepository.save(project);
        log.info("Update the status of the project with id = {}", updatedProject.getId());
        return projectMapper.toProjectDto(updatedProject);
    }

    @Override
    public ProjectDto findById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(new Project());
        return projectMapper.toProjectDto(project);
    }

    @Override
    public List<ProjectDto> search(SearchProjectFilterDto filter) {
        List<Project> projects = projectRepository.findAll(ProjectSpecification.getSpec(filterMapper.fromDto(filter)));
        return projects
                .stream()
                .map(projectMapper::toProjectDto)
                .toList();
    }

    @Override
    public List<MemberDto> listAllMembers(Long projectId) {
        return projectTeamService.listAllMembers(projectId);
    }

    @Transactional
    @Override
    public List<MemberDto> addMember(Long projectId, Long memberId, Role role) {
        if(!isProjectExist(projectId)){
            throw new NoSuchElementException(String.format("Project with id = %d not exists", projectId));
        }
        if(!memberService.isMemberExist(memberId)){
            throw new NoSuchElementException(String.format("Member with id = %d not exists", memberId));
        }
        projectTeamService.addMember(projectId, memberId, role);
        log.info("Add member with id = {} to the project with id = {}", memberId, projectId);
        return projectTeamService.listAllMembers(projectId);
    }

    @Transactional
    @Override
    public List<MemberDto> deleteMember(Long projectId, Long memberId) {
        projectTeamService.deleteMember(projectId, memberId);
        log.info("Delete member with id = {} from the project with id = {}", memberId, projectId);
        return projectTeamService.listAllMembers(projectId);
    }

    private boolean isProjectExist(Long id){
        return projectRepository.findById(id).isPresent();
    }
}
