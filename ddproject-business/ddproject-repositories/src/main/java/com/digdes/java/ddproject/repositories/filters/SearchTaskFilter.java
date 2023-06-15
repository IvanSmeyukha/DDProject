package com.digdes.java.ddproject.repositories.filters;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchTaskFilter {
//    Task's id
    private String title;
//    Task's deadline min value
    private OffsetDateTime deadlineMin;
//    Task's deadline max value
    private OffsetDateTime deadlineMax;
//    Task's creation date min value
    private OffsetDateTime creationDateMin;
//    Task's creation date max value
    private OffsetDateTime creationDateMax;
//    Task's author id
    private Long authorId;
//    Task's executor id
    private Long executorId;
//    Task's statuses
    private List<TaskStatus> statuses;
}
