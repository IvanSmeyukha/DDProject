package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import com.digdes.java.ddproject.dto.error.ApiErrorResponse;
import com.digdes.java.ddproject.dto.filters.SearchTaskFilterDto;
import com.digdes.java.ddproject.dto.task.BaseTaskDto;
import com.digdes.java.ddproject.dto.task.ExtTaskDto;
import com.digdes.java.ddproject.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create new task",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ExtTaskDto create(@RequestBody @Valid BaseTaskDto dto) {
        return taskService.create(dto);
    }

    @Operation(summary = "Update task",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ExtTaskDto update(@PathVariable Long id,
                             @RequestBody @Valid BaseTaskDto dto
    ) {
        return taskService.update(id, dto);
    }

    @Operation(summary = "Update task status",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PutMapping(value = "/{id}/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ExtTaskDto updateStatus(@PathVariable Long id,
                                   @RequestParam TaskStatus status
    ) {
        return taskService.updateStatus(id, status);
    }

    @Operation(summary = "Get tasks matching the conditions filter",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<BaseTaskDto> search(@RequestBody SearchTaskFilterDto dto) {
        return taskService.search(dto);
    }
}
