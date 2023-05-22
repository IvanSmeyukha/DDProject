package com.digdes.java.ddproject.dto.project;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProjectDto {
    private Long id;
    private String title;
    private String description;
}
