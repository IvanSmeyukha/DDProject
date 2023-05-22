package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Member implements Serializable{
//    Member's id
    private Long id;
//    Member's first name
    private String firstName;
//    Member's last name
    private String lastName;
//    Member's surname
    private String surName;
//    Member's working position
    private String position;
//    Member's unique account number between active members
    private Long account;
//    Member's email
    private String email;
//    Member's status. May be: ACTIVE, DELETED
    private MemberStatus status;
}
