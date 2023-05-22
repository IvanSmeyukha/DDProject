package com.digdes.java.ddproject.dto.filters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMemberFilter {
    // TODO: comment all dto's fields
    private String firstName;
    private String lastName;
    private String surName;
    private String position;
    private String email;
    private Long projectId;
}
