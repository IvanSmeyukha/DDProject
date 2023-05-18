package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@RequiredArgsConstructor
public class BaseTaskDto {
    private Long id;
    private String title;
    private String description;
    private Long laborHours;
    private OffsetDateTime deadline;
    private TaskStatus status;
    private Long authorId;
    private Long executorId;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;

}
