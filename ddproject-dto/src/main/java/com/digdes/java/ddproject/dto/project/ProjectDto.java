package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project's info")
public class ProjectDto {
    @Schema(description = "Project's id")
    @NotNull(message = "Id can't be null")
    private Long id;
    @Schema(description = "Project's title")
    private String title;
    @Schema(description = "Project's description")
    private String description;
    @Schema(description = "Project's status")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProjectStatus status;
}
