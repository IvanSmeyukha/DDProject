package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectTeam {
//    Project participant's id
    private Long memberId;
//    Project's id
    private Long projectId;
//    Participant's role in project. May be: PROJECT_MANAGER, ANALYST, DEVELOPER, TESTER
    private Role role;
}
