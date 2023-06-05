package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Project's info")
public class AddMemberToProjectDto {
    private Long memberId;
    private Role role;
}
