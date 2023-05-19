package com.digdes.java.ddproject.dto.projectTeam;

import com.digdes.java.ddproject.dto.member.MemberDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProjectTeamDto {
    private List<MemberDto> team;
}
