package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProjectStatusDto {
    @NotNull(message = "Id can't be null")
    private Long id;
    @NotNull(message = "Status can't be null")
    private ProjectStatus status;
}
