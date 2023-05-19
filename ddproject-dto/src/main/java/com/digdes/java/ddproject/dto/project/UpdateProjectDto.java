package com.digdes.java.ddproject.dto.project;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProjectDto {
    private Long code;
    private String title;
    private String description;
}
