package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Extended task's info")
public class ExtTaskDto extends BaseTaskDto{
    @Schema(description = "Task's executor info")
    private MemberDto executor;
    @Schema(description = "Task's author info")
    private MemberDto author;
    @Schema(description = "Task's project info")
    private ProjectDto project;
}
