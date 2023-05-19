package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {
    private Long code;
    private String title;
    private String description;
    private ProjectStatus status;
}
