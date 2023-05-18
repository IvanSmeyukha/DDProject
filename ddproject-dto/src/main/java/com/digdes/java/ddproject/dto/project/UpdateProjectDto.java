package com.digdes.java.ddproject.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateProjectDto {
    private Long code;
    private String title;
    private String description;
}
