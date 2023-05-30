package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExtTaskDto extends BaseTaskDto{
    private MemberDto executor;
    private MemberDto author;
    private ProjectDto project;
}
