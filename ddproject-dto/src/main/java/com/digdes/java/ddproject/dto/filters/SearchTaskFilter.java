package com.digdes.java.ddproject.dto.filters;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Filter for tasks searching")
public class SearchTaskFilter {
    @Schema(description = "Task's id")
    private String title;
    @Schema(description = "Task's deadline min value")
    private OffsetDateTime deadlineMin;
    @Schema(description = "Task's deadline max value")
    private OffsetDateTime deadlineMax;
    @Schema(description = "Task's creation date min value")
    private OffsetDateTime creationDateMin;
    @Schema(description = "Task's creation date max value")
    private OffsetDateTime creationDateMax;
    @Schema(description = "Task's author id")
    private Long authorId;
    @Schema(description = "Task's executor id")
    private Long executorId;
    @Schema(description = "Task's statuses")
    private List<TaskStatus> statuses;
}
