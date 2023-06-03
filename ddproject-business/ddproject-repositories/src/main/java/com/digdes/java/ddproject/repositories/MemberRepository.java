package com.digdes.java.ddproject.repositories;

import com.digdes.java.ddproject.dto.filters.SearchMemberFilter;
import com.digdes.java.ddproject.dto.member.AddMemberToProjectDto;
import com.digdes.java.ddproject.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> create(Member member);

    Optional<Member> update(Member member);

    Optional<Member> getById(Long id);


    Optional<List<Member>> search(SearchMemberFilter filter);

    Optional<Member> deleteById(Long id);

    Optional<Member> addToProject(AddMemberToProjectDto addMember);
}
