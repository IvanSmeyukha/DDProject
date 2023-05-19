package com.digdes.java.ddproject.dto.member;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String surName;
    private String position;
    private Long account;
    private String email;
    private MemberStatus status;
}
