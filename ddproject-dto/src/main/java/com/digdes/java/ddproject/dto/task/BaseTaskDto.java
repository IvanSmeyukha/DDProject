package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
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
public class BaseTaskDto {
    private Long id;
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Long laborHours;
    @NotNull
    private OffsetDateTime deadline;
    private TaskStatus status;
    @NotNull
    private Long authorId;
    private Long executorId;
    private Long projectId;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;
}
