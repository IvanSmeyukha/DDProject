package com.digdes.java.ddproject.mapping.member;

import com.digdes.java.ddproject.common.enums.MemberStatus;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.mapping.useraccount.UserAccountMapper;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class MemberMapper {
    private final UserAccountMapper userAccountMapper;

    public Member fromMemberDto(MemberDto memberDto){
        return Member.builder()
                .id(memberDto.getId())
                .firstName(memberDto.getFirstName())
                .lastName(memberDto.getLastName())
                .patronymic(memberDto.getPatronymic())
                .account(UserAccount.builder().id(memberDto.getAccount()).build())
                .position(memberDto.getPosition())
                .email(memberDto.getEmail())
                .status(memberDto.getStatus())
                .build();
    }

    public MemberDto toMemberDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .patronymic(member.getPatronymic())
                .account(member.getAccount().getId())
                .position(member.getPosition())
                .email(member.getEmail())
                .status(member.getStatus())
                .build();
    }

    public Member fromResultSet(ResultSet resultSet) throws SQLException {
        return Member.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .patronymic(resultSet.getString("sur_name"))
                .account(UserAccount.builder().id(resultSet.getLong("account")).build())
                .position(resultSet.getString("position"))
                .email(resultSet.getString("email"))
                .status(MemberStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}
