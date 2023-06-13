package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilter;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.dto.project.UpdateProjectStatusDto;

import java.util.List;

public interface ProjectService {
    ProjectDto create(ProjectDto dto);

    ProjectDto update(Long id, ProjectDto dto);

    ProjectDto updateStatus(Long id, ProjectStatus status);

    List<ProjectDto> search(SearchProjectFilter filter);

    ProjectDto getById(Long projectId);

    List<MemberDto> listAllMembers(Long projectId);

    List<MemberDto> addMember(Long projectId, Long memberId, Role role);

    List<MemberDto> deleteMember(Long projectId, Long memberId);
}
