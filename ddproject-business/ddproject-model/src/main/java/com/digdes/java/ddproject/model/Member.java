package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Member implements Serializable{
    private Long id;
    private String firstName;
    private String lastName;
    private String surName;
    private String position;
    private Long account;
    private String email;
    private MemberStatus status;
}
