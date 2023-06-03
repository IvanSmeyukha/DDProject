package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.model.ProjectTeam;

import java.util.List;

public interface ProjectTeamService {
    ProjectTeam addMember(Long projectId, AddMemberToProjectDto dto);

    List<MemberDto> listAllMembers(Long projectId);

    ProjectTeam deleteMember(Long projectId, Long memberId);

    boolean checkMember(Long projectId, Long memberId);
}
