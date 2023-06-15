package com.digdes.java.ddproject.repositories.filters;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchProjectFilter {
//    Project's id
    private Long id;
//    Project's title
    private String title;
//    Project's statuses
    private List<ProjectStatus> statuses;
}
