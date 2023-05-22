package com.digdes.java.ddproject.dto.project;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListsProjectsDto {
    private Long id;
    private String title;
    private List<ProjectStatus> statusList;
}
