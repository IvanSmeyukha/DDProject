package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.dto.filters.SearchTaskFilter;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.dto.task.UpdateTaskStatusDto;

import java.util.List;

public interface TaskService {

    ExtTaskDto create(BaseTaskDto dto);

    ExtTaskDto update(BaseTaskDto dto);

    ExtTaskDto updateStatus(UpdateTaskStatusDto dto);

    List<BaseTaskDto> search(SearchTaskFilter filter);
}
