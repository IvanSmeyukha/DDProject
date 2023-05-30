package com.digdes.java.ddproject.dto.filters;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchProjectFilter {
    private Long id;
    private String title;
    private List<ProjectStatus> statuses;
}
