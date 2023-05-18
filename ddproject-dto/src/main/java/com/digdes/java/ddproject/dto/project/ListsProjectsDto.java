package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ListsProjectsDto {
    private Long code;
    private String title;
    private List<ProjectStatus> statusList;
}
