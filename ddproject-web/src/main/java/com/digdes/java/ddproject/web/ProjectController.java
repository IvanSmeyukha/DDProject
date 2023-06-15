package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.error.ApiErrorResponse;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Tag(name = "project", description = "Project controller")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Create new project",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProjectDto create(@RequestBody @Valid ProjectDto dto) {
        return projectService.create(dto);
    }

    @Operation(summary = "Update project",
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
    public ProjectDto update(@PathVariable Long id,
                             @RequestBody @Valid ProjectDto dto
    ) {
        return projectService.update(id, dto);
    }

    @Operation(summary = "Update project status",
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
    public ProjectDto updateStatus(@PathVariable Long id,
                                   @RequestParam ProjectStatus status
    ) {
        return projectService.updateStatus(id, status);
    }

    @Operation(summary = "Get projects matching the conditions filter",
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
    public List<ProjectDto> search(@RequestBody SearchProjectFilterDto filter) {
        return projectService.search(filter);
    }

    @Operation(summary = "Get project by id",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping(value = "/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProjectDto getById(@PathVariable Long projectId) {
        return projectService.findById(projectId);
    }

    @Operation(summary = "Get members who are working on the project",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping(value = "/{projectId}/team",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MemberDto> listProjectTeam(@PathVariable Long projectId) {
        return projectService.listAllMembers(projectId);
    }

    @Operation(summary = "Add member to the project",
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
    @PostMapping(value = "/{projectId}/team",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MemberDto> addMember(@PathVariable Long projectId,
                                     @RequestParam Long memberId,
                                     @RequestParam Role role
    ) {
        return projectService.addMember(projectId, memberId, role);
    }

    @Operation(summary = "Delete member from the project",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @DeleteMapping(value = "/{projectId}/team/{memberId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MemberDto> deleteMember(@PathVariable Long projectId,
                                        @PathVariable Long memberId) {
        return projectService.deleteMember(projectId, memberId);
    }
}
