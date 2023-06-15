package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.model.ProjectTeam;

import java.util.List;

public interface ProjectTeamService {
    ProjectTeam addMember(Long projectId, Long memberId, Role role);

    List<MemberDto> listAllMembers(Long projectId);

    ProjectTeam deleteMember(Long projectId, Long memberId);

    boolean isMemberInProject(Long projectId, Long memberId);
}
