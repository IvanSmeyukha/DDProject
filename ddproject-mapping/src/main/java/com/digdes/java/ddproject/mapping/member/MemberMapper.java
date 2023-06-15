package com.digdes.java.ddproject.mapping.member;

import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


@Component
public class MemberMapper {

    public Member fromMemberDto(MemberDto memberDto) {
        Member member = Member.builder()
                .id(memberDto.getId())
                .firstName(memberDto.getFirstName())
                .lastName(memberDto.getLastName())
                .patronymic(memberDto.getPatronymic())
                .position(memberDto.getPosition())
                .email(memberDto.getEmail())
                .status(memberDto.getStatus())
                .build();
        if (!ObjectUtils.isEmpty(memberDto.getAccount())) {
            member.setAccount(UserAccount.builder().id(memberDto.getAccount()).build());
        }
        return member;
    }

    public MemberDto toMemberDto(Member member) {
        MemberDto dto = MemberDto.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .patronymic(member.getPatronymic())
                .position(member.getPosition())
                .email(member.getEmail())
                .status(member.getStatus())
                .build();
        if(!ObjectUtils.isEmpty(member.getAccount())){
            dto.setAccount(member.getAccount().getId());
        }
        return dto;
    }
}
