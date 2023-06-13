package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.common.exceptions.NullIdException;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilter;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.dto.project.UpdateProjectStatusDto;
import com.digdes.java.ddproject.mapping.project.ProjectMapper;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectSpecification;
import com.digdes.java.ddproject.services.ProjectService;
import com.digdes.java.ddproject.services.ProjectTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceJpa implements ProjectService {
    private final ProjectRepositoryJpa projectRepository;
    private final ProjectTeamService projectTeamService;
    private final MemberServiceJpa memberServiceJpa;
    private final ProjectMapper projectMapper;

    @Transactional
    @Override
    public ProjectDto create(ProjectDto dto) {
        Project project = projectMapper.fromProjectDto(dto);
        if(projectRepository.findById(project.getId()).isPresent()){
            throw new DuplicateKeyException(String.format("Project with id = %d already exists", project.getId()));
        }
        project.setStatus(ProjectStatus.DRAFT);
        return projectMapper.toProjectDto(projectRepository.save(project));
    }

    @Transactional
    @Override
    public ProjectDto update(Long id, ProjectDto dto) {
        if(projectRepository.findById(id).isEmpty()){
            throw new NoSuchElementException(String.format("Project with id = %d not exists", id));
        }
        Project project = projectMapper.fromProjectDto(dto);
        project.setId(id);
        return projectMapper.toProjectDto(projectRepository.save(project));
    }

    @Transactional
    @Override
    public ProjectDto updateStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format("Project with id = %d not exists", id))
        );
        project.setStatus(status);
        return projectMapper.toProjectDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto getById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(new Project());
        return projectMapper.toProjectDto(project);
    }

    @Override
    public List<ProjectDto> search(SearchProjectFilter filter) {
        List<Project> projects = projectRepository.findAll(ProjectSpecification.getSpec(filter));
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
        memberServiceJpa.findById(memberId);
        if(!isProjectExist(projectId)){
            throw new NoSuchElementException(String.format("Project with id = %d not exists", projectId));
        }
        if(!memberServiceJpa.isMemberExist(memberId)){
            throw new NoSuchElementException(String.format("Member with id = %d not exists", memberId));
        }
        projectTeamService.addMember(projectId, memberId, role);
        return projectTeamService.listAllMembers(projectId);
    }

    @Transactional
    @Override
    public List<MemberDto> deleteMember(Long projectId, Long memberId) {
        projectTeamService.deleteMember(projectId, memberId);
        return projectTeamService.listAllMembers(projectId);
    }

    public boolean isProjectExist(Long id){
        return projectRepository.findById(id).isPresent();
    }
}
