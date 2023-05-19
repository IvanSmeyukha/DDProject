package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Project;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtTaskDto extends BaseTaskDto{
    private Member executor;
    private Member author;
    private Project project;
}
