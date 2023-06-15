package com.digdes.java.ddproject.repositories.filters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchMemberFilter {
//    Member's first name
    private String firstName;
//    Member's last name
    private String lastName;
//    Member's patronymic
    private String patronymic;
//    Member's position
    private String position;
//    Member's email
    private String email;
}
