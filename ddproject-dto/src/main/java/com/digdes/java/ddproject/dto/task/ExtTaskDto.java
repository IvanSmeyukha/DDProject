package com.digdes.java.ddproject.dto.task;

import com.digdes.java.ddproject.model.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ExtTaskDto extends BaseTaskDto{
    private Member executor;
    private Member author;
}
