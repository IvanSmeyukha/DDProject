package com.digdes.java.ddproject.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
public class CreateTaskDto{
    private String title;
    private String description;
    private Long laborHours;
    private OffsetDateTime deadline;
    private Long projectId;
    private Long authorId;
}
