package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @NotNull(message = "Id can't be null")
    private Long id;
    private String title;
    private String description;
    private ProjectStatus status;
}
