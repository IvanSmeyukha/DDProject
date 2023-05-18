package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ListTasksDto {
    private String title;
    private OffsetDateTime deadlineMin;
    private OffsetDateTime deadlineMax;
    private OffsetDateTime creationDateMin;
    private OffsetDateTime creationDateMax;
    private Long authorId;
    private Long executorId;
    private List<TaskStatus> statusList;
}
