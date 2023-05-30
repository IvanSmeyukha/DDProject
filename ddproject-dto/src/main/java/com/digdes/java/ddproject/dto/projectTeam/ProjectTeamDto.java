package com.digdes.java.ddproject.dto.projectTeam;

import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProjectTeamDto {
    private ProjectDto project;
    private List<MemberDto> team;
}
