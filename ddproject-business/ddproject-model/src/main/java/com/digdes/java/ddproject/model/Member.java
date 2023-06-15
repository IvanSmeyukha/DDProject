package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member implements Serializable{
//    Member's id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
//    Member's first name
    @Column(name = "first_name")
    private String firstName;
//    Member's last name
    @Column(name = "last_name")
    private String lastName;
//    Member's surname
    @Column(name = "patronymic")
    private String patronymic;
//    Member's working position
    @Column(name = "position")
    private String position;
//    Member's unique account number between active members
    @OneToOne
    @JoinColumn(name = "account")
    private UserAccount account;
//    Member's email
    @Column(name = "email")
    private String email;
//    Member's status. May be: ACTIVE, DELETED
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status;
}
