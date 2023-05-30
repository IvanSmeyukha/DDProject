package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddMemberToProjectDto {
    private Long memberId;
    private Role role;
}
