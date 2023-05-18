package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.Role;
import lombok.Data;

@Data
public class ProjectTeam {
    private Long memberId;
    private Long projectId;
    private Role role;
}