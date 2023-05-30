package com.digdes.java.ddproject.dto.filters;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class SearchTaskFilter {
    private String title;
    private OffsetDateTime deadlineMin;
    private OffsetDateTime deadlineMax;
    private OffsetDateTime creationDateMin;
    private OffsetDateTime creationDateMax;
    private Long authorId;
    private Long executorId;
    private List<TaskStatus> statuses;
}
