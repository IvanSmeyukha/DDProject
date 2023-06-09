package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task's info")
public class BaseTaskDto {
    @Schema(description = "Task's id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "Title is required field")
    @Schema(description = "Task's title")
    private String title;
    @Schema(description = "Task's description")
    private String description;
    @Schema(description = "Task's labor hours")
    @NotNull(message = "Labor hours is required field")
    private Long laborHours;
    @Schema(description = "Task's deadline")
    @NotNull(message = "Deadline is required field")
    private OffsetDateTime deadline;
    @Schema(description = "Task's status")
    private TaskStatus status;
    @Schema(description = "Task's author id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorId;
    @Schema(description = "Task's executor id")
    private Long executorId;
    @Schema(description = "Id of the project to which the task is attached")
    @NotNull(message = "Project id is required field")
    private Long projectId;
    @Schema(description = "Task's creation date")
    private OffsetDateTime creationDate;
    @Schema(description = "Last date when Task was updated")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime lastUpdateDate;
}
