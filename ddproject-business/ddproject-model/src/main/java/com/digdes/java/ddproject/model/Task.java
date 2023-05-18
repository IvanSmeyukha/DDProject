package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Task {
    private Long id;
    private String title;
    private String description;
    private Member executor;
    private Long laborHours;
    private OffsetDateTime deadline;
    private TaskStatus status;
    private Member author;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;
}
