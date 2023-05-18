package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateProjectStatusDto {
    private ProjectStatus status;
}
