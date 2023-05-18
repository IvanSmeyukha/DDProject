package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTaskStatusDto {
    private TaskStatus status;
}
