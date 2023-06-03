package com.digdes.java.ddproject.dto.member;

import com.digdes.java.ddproject.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddMemberToProjectDto {
    private Long memberId;
    private Long projectId;
    private Role role;
}
