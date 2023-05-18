package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private Member executor;
    private Project project;
    private Long laborHours;
    private OffsetDateTime deadline;
    private TaskStatus status;
    private Member author;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastUpdateDate;
}
