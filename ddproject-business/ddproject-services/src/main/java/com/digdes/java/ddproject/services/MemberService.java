package com.digdes.java.ddproject.services;

import com.digdes.java.ddproject.dto.filters.SearchMemberFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;

import java.util.List;


public interface MemberService {

    MemberDto create(MemberDto memberDto);

    MemberDto update(Long id, MemberDto memberDto);

    MemberDto delete(Long id);

    MemberDto findById(Long id);

    MemberDto findByAccountUsername(String username);

    List<MemberDto> search(SearchMemberFilterDto filter);

    boolean isMemberExist(Long id);
}
