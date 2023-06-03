package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {
//    Project's id
    private Long id;
//    Short name of the project
    private String title;
//    More detailed information about the project
    private String description;
//    Project's status. May be: DRAFT -> DEVELOPMENT -> TESTING -> RELEASE
    private ProjectStatus status;
}
