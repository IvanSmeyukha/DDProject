package com.digdes.java.ddproject.dto.filters;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Filter for projects searching")
public class SearchProjectFilter {
    @Schema(description = "Project's id")
    private Long id;
    @Schema(description = "Project's title")
    private String title;
    @Schema(description = "Project's statuses")
    private List<ProjectStatus> statuses;
}
