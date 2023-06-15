package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto create(ProjectDto dto);

    ProjectDto update(Long id, ProjectDto dto);

    ProjectDto updateStatus(Long id, ProjectStatus status);

    List<ProjectDto> search(SearchProjectFilterDto filter);

    ProjectDto findById(Long projectId);

    List<MemberDto> listAllMembers(Long projectId);

    List<MemberDto> addMember(Long projectId, Long memberId, Role role);

    List<MemberDto> deleteMember(Long projectId, Long memberId);

    boolean isProjectExist(Long projectId);
}
