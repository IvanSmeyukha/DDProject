package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProjectStatusDto {
    private ProjectStatus status;
}
