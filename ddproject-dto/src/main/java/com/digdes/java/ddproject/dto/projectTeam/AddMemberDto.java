package com.digdes.java.ddproject.dto.projectTeam;

import com.digdes.java.ddproject.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddMemberDto {
    private Long memberId;
    private Long projectCode;
    private Role role;
}
