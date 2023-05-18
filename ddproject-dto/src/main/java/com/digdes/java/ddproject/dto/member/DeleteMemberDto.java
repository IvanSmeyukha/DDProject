package com.digdes.java.ddproject.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DeleteMemberDto {
    private Long id;
    private Long account;
}
