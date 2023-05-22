package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Task {
//    Task's id
    private Long id;
//    Brief information about the task
    private String title;
//    More detailed information about the task
    private String description;
//    Member responsible for this task
    private Member executor;
//    Project to which the task is attached
    private Project project;
//    Time to complete the task in hours
    private Long laborHours;
//    Deadline by which the task must be completed
    private OffsetDateTime deadline;
//    Task's status. May be:  NEW, IN_WORK, COMPLETED, CLOSED
    private TaskStatus status;
//    Member who last modified the task
    private Member author;
//    Task's creation date
    private OffsetDateTime creationDate;
//    Task's last update date
    private OffsetDateTime lastUpdateDate;
}
