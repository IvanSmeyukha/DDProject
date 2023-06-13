package com.digdes.java.ddproject.services.jpa;

import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.model.ProjectTeam;
import com.digdes.java.ddproject.repositories.jpa.ProjectTeamRepositoryJpa;
import com.digdes.java.ddproject.services.ProjectTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTeamServiceJpa implements ProjectTeamService {
    private final ProjectTeamRepositoryJpa projectTeamRepository;
    private final MemberMapper memberMapper;

    @Override
    public ProjectTeam addMember(Long projectId, Long memberId, Role role) {
        ProjectTeam projectTeam = new ProjectTeam(projectId, memberId, role);
        return projectTeamRepository.save(projectTeam);
    }

    @Override
    public List<MemberDto> listAllMembers(Long projectId) {
        return projectTeamRepository
                .findByProjectId(projectId)
                .stream()
                .map(memberMapper::toMemberDto)
                .toList();
    }

    @Override
    public ProjectTeam deleteMember(Long projectId, Long memberId) {
        ProjectTeam projectTeam = new ProjectTeam(projectId, memberId);
        projectTeamRepository.deleteById(projectTeam.getId());
        return projectTeam;
    }

    @Override
    public boolean checkMember(Long projectId, Long memberId){
        ProjectTeam projectTeam = new ProjectTeam(projectId, memberId);
        return projectTeamRepository.findById(projectTeam.getId()).isPresent();
    }
}
