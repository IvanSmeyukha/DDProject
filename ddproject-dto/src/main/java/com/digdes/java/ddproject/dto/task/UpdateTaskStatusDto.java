package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateTaskStatusDto {
    private TaskStatus status;
}
