package com.digdes.java.ddproject.web;

import com.digdes.java.ddproject.dto.filters.SearchProjectFilter;
import com.digdes.java.ddproject.dto.project.AddMemberToProjectDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.dto.project.UpdateProjectStatusDto;
import com.digdes.java.ddproject.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/project")
    public ProjectDto create(@RequestBody @Valid ProjectDto dto) {
        return projectService.create(dto);
    }

    @PutMapping("/project")
    public ProjectDto update(@RequestBody @Valid ProjectDto dto) {
        return projectService.update(dto);
    }

    @PutMapping("/project/status")
    public ProjectDto updateStatus(@RequestBody @Valid UpdateProjectStatusDto dto) {
        return projectService.updateStatus(dto);
    }

    @GetMapping("/project")
    public List<ProjectDto> search(@RequestBody SearchProjectFilter filter) {
        return projectService.search(filter);
    }

    @GetMapping("/project/{projectId}")
    public ProjectDto getById(@PathVariable Long projectId) {
        return projectService.getById(projectId);
    }

    @GetMapping("/project/{projectId}/team")
    public List<MemberDto> listProjectTeam(@PathVariable Long projectId) {
        return projectService.listAllMembers(projectId);
    }

    @PostMapping("/project/{projectId}/team")
    public List<MemberDto> addMember(@PathVariable Long projectId,
                                @RequestBody AddMemberToProjectDto dto) {
        return projectService.addMember(projectId, dto);
    }

    @DeleteMapping("/project/{projectId}/team/{memberId}")
    public List<MemberDto> deleteMember(@PathVariable Long projectId,
                                   @PathVariable Long memberId) {
        return projectService.deleteMember(projectId, memberId);
    }
}
