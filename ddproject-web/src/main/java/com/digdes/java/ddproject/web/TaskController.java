package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.dto.filters.SearchTaskFilter;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.dto.task.UpdateTaskStatusDto;
import com.digdes.java.ddproject.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/task")
    public ExtTaskDto create(@RequestBody BaseTaskDto dto) {
        return taskService.create(dto);
    }

    @PutMapping("/task")
    public ExtTaskDto update(@RequestBody BaseTaskDto dto) {
        return taskService.update(dto);
    }

    @PutMapping("/task/status")
    public ExtTaskDto updateStatus(@RequestBody UpdateTaskStatusDto dto) {
        return taskService.updateStatus(dto);
    }

    @GetMapping("/task")
    public List<BaseTaskDto> search(@RequestBody SearchTaskFilter dto) {
        return taskService.search(dto);
    }
}
