package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.dto.filters.SearchProjectFilter;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.dto.project.UpdateProjectStatusDto;

import java.util.List;

public interface ProjectService {
    ProjectDto create(ProjectDto dto);

    ProjectDto update(ProjectDto dto);

    ProjectDto updateStatus(UpdateProjectStatusDto dto);

    List<ProjectDto> search(SearchProjectFilter filter);

    ProjectDto getById(Long projectId);

    List<MemberDto> listAllMembers(Long projectId);

    List<MemberDto> addMember(Long projectId, AddMemberToProjectDto addMemberToProjectDto);

    List<MemberDto> deleteMember(Long projectId, Long memberId);
}
