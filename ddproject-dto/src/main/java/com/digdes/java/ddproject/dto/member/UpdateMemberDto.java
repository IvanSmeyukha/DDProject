package com.digdes.java.ddproject.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateMemberDto {
    private String firstName;
    private String lastName;
    private String surName;
    private String position;
    private Long account;
    private String email;
}
