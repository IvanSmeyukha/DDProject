package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.dto.filters.SearchTaskFilterDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;

import java.util.List;

public interface TaskService {

    ExtTaskDto create(BaseTaskDto dto);

    ExtTaskDto update(Long id, BaseTaskDto dto);

    ExtTaskDto updateStatus(Long id, TaskStatus status);

    List<BaseTaskDto> search(SearchTaskFilterDto filter);
}
